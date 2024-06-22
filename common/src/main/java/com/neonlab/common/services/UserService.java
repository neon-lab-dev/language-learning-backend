package com.neonlab.common.services;

import com.neonlab.common.annotations.Loggable;
import com.neonlab.common.dto.AddressDto;
import com.neonlab.common.dto.SignUpRequest;
import com.neonlab.common.dto.UserAddressDto;
import com.neonlab.common.dto.UserDto;
import com.neonlab.common.entities.AuthUser;
import com.neonlab.common.entities.User;
import com.neonlab.common.expectations.InvalidInputException;
import com.neonlab.common.expectations.ServerException;
import com.neonlab.common.models.PageableResponse;
import com.neonlab.common.models.UserReportModel;
import com.neonlab.common.repositories.UserRepository;
import com.neonlab.common.searchcriteria.UserSearchCriteria;
import com.neonlab.common.specifications.UserSpecification;
import com.neonlab.common.utilities.ObjectMapperUtils;
import com.neonlab.common.utilities.PageableUtils;
import com.neonlab.common.utilities.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@Loggable
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final AuthUserService authUserService;

    public User createUser(SignUpRequest request) throws ServerException {
        var retVal = new User(request.getPhoneNo(), request.getPhoneNo());
        retVal = ObjectMapperUtils.map(request,User.class);
        return userRepository.save(retVal);
    }

    public boolean isExistingUser(String phoneNo){
        try {
            var user = fetchByPhoneNo(phoneNo);
            return true;
        } catch (InvalidInputException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    public UserDto fetch() throws ServerException, InvalidInputException {
        var user = getLoggedInUser();
        return ObjectMapperUtils.map(user, UserDto.class);
    }

    public UserDto update(UserDto userDto) throws ServerException, InvalidInputException {
        var user = getLoggedInUser();
        ObjectMapperUtils.map(userDto,user);
        user = userRepository.save(user);
        return ObjectMapperUtils.map(user,UserDto.class);
    }

    public User fetchByPhoneNo(String phone) throws InvalidInputException {
        return userRepository.findByPhoneNo(phone)
                .orElseThrow(() -> new InvalidInputException(String.format("User not found with primary Phone no %s", phone)));
    }

    public User fetchBySecondaryPhoneNo(String phone) throws InvalidInputException {
        return userRepository.findBySecondaryPhoneNo(phone)
                .orElseThrow(() -> new InvalidInputException(String.format("User not found with secondary Phone no %s", phone)));
    }

    public User fetchByPhoneNoOrEmail(String phone, String email) throws InvalidInputException {
        return userRepository.findByPhoneNoOrEmail(phone, email)
                .orElseThrow(() -> new InvalidInputException(String.format("User not found with primary Phone no %s or email %s", phone, email)));
    }

    public User fetchByEmail(String email) throws InvalidInputException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidInputException(String.format("User not found with email %s", email)));
    }

    public User fetchEitherByPrimaryPhoneOrEmail(String searchParam) throws InvalidInputException {
        return userRepository.findEitherByPrimaryPhoneNoOrEmail(searchParam)
                .orElseThrow(() ->
                        new InvalidInputException(String.format("User not found with either primary phone no or email as %s", searchParam)));
    }

    public User fetchById(String id) throws InvalidInputException {
        return userRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException(
                        String.format("User not found with Id %s", id)));
    }

    public User getLoggedInUser() throws InvalidInputException {
        var principal = SecurityUtils.getLoggedInUser();
        if (principal instanceof AuthUser authUser){
            return fetchById(authUser.getUserId());
        }
        log.warn("No user logged in");
        return null;
    }

    public boolean existingContactNo(String contactNo){
        Optional<User> userMayBe = userRepository.findByPhoneNo(contactNo);
        return userMayBe.isPresent();
    }

    public boolean existingEmail(String email){
        Optional<User> userMayBe = userRepository.findByEmail(email);
        return userMayBe.isPresent();
    }

    public PageableResponse<UserAddressDto> fetchByAdmin(UserSearchCriteria searchCriteria) {
        var pageable = PageableUtils.createPageable(searchCriteria);
        Page<User> users = userRepository.findAll(
                UserSpecification.buildSearchCriteria(searchCriteria)
                ,pageable
        );

        var resultList = users.getContent().stream()
                .map(this::toUserAddressDto)
                .filter(Objects::nonNull)
                .toList();

        return new PageableResponse<>(resultList,searchCriteria);
    }

    private UserAddressDto toUserAddressDto(final User user){
        try {
            var addressList = user.getAddresses().stream()
                    .map(address -> {
                        try {
                            return ObjectMapperUtils.map(address, AddressDto.class);
                        } catch (ServerException e) {
                            return null;
                        }
                    })
                    .filter(Objects::nonNull)
                    .toList();
            var userDto = ObjectMapperUtils.map(user, UserDto.class);
            return new UserAddressDto(userDto, addressList);
        } catch (ServerException e) {
            log.warn("Error while paring to user address details. Error: {}",e.getMessage(),e);
            return null;
        }
    }

    public UserReportModel getReport(){
        var totalUsers = userRepository.count(
                UserSpecification.buildSearchCriteria(
                        UserSearchCriteria.userSearchCriteriaBuilder().build()
                    )
                );
        var totalActiveUsers =  authUserService.fetchActiveUserCount();
        return new UserReportModel(totalUsers, totalActiveUsers);
    }

}
