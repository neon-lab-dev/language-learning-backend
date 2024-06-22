package com.neonlab.common.specifications;
import com.neonlab.common.entities.User;
import com.neonlab.common.searchcriteria.UserSearchCriteria;
import com.neonlab.common.utilities.StringUtil;
import org.springframework.data.jpa.domain.Specification;
import static com.neonlab.common.constants.GlobalConstants.Symbols.PERCENTAGE;
import static com.neonlab.common.constants.UserEntityConstant.*;



public class UserSpecification {

    public static Specification<User> buildSearchCriteria(final UserSearchCriteria searchCriteria){
        var retVal = Specification.<User>where(null);
        if(!StringUtil.isNullOrEmpty(searchCriteria.getId())){
            retVal = retVal.and(filterById(searchCriteria.getId()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getChildName())){
            retVal = retVal.and(filterByChildNameLike(searchCriteria.getChildName()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getNicName())){
            retVal = retVal.and(filterByNicNameLike(searchCriteria.getNicName()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getEmail())){
            retVal = retVal.and(filterByEmailLike(searchCriteria.getEmail()));
        }
        if(!StringUtil.isNullOrEmpty(searchCriteria.getPhoneNo())){
            retVal = retVal.and(filterByPhoneNo(searchCriteria.getPhoneNo()));
        }
        if(!StringUtil.isNullOrEmpty(String.valueOf(searchCriteria.getAge()))){
            retVal = retVal.and(filterByAge(String.valueOf(searchCriteria.getAge())));
        }
        if(!StringUtil.isNullOrEmpty(String.valueOf(searchCriteria.getGender()))){
            retVal = retVal.and(filterByGenderLike(String.valueOf(searchCriteria.getGender())));
        }
        return retVal;
    }

    private static Specification<User> filterById(final String id){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(ID),id))
        );
    }

    private static Specification<User> filterByPhoneNo(final String phoneNo){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(PHONE_NO),phoneNo))
        );
    }

    private static Specification<User> filterByGenderLike(final String gender){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(GENDER),gender))
        );
    }


    private static Specification<User> filterByAge(final String age){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get(AGE),withLikePattern(age)))
        );
    }

    private static Specification<User> filterByNicNameLike(final String nicName){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NIC_NAME),withLikePattern(nicName)))
        );
    }
    private static Specification<User> filterByChildNameLike(final String nicName){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(NIC_NAME),withLikePattern(nicName)))
        );
    }

    private static Specification<User> filterByEmailLike(final String email){
        return (((root, query, criteriaBuilder) ->
                criteriaBuilder.like(root.get(EMAIL),withLikePattern(email)))
        );
    }

    private static String withLikePattern(String str){
        return PERCENTAGE + str + PERCENTAGE;
    }
}
