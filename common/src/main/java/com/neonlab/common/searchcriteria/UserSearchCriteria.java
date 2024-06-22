package com.neonlab.common.searchcriteria;
import com.neonlab.common.models.searchCriteria.PageableSearchCriteria;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class UserSearchCriteria extends PageableSearchCriteria {
    /**
     *  filter by id
     */
    private String id;
    /**
     *  Filter by child_name
     */
    private String childName;
    /**
     *  Filter by nic_name
     */
    private String nicName;
    /**
     *  Filter by age
     */
    private Integer age;
    /**
     *  Filter by gender
     */
    private String gender;
    /**
     *  filter by email
     */
    private String email;
    /**
     *  filter by phoneNo
     */
    private String phoneNo;


    @Builder(builderMethodName="userSearchCriteriaBuilder")
    public UserSearchCriteria(
            final String id,
            final String childName,
            final String nicName,
            final Integer age,
            final String gender,
            final String email,
            final String phoneNo,
            final int perPage,
            final int pageNo,
            final String sortBy,
            final Sort.Direction sortDirection
    ){
        super(perPage, pageNo, sortBy, sortDirection);
        this.id = id;
        this.childName = childName;
        this.nicName = nicName;
        this.age = age;
        this.gender = gender;
        this.email = email;
        this.phoneNo = phoneNo;
    }
}
