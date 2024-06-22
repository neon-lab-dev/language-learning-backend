package com.neonlab.common.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.neonlab.common.enums.Gender;
import com.neonlab.common.utilities.JsonUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

//@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@Table(name = "user", indexes = {
        @Index(name = "idx_email", columnList = "email"),
        @Index(name = "idx_phone_no", columnList = "phone_no")
})
public class User extends Generic {

    public User(){
        super();
    }

    public User(String createdBy, String modifiedBy){
        super(createdBy,modifiedBy);
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "child_name")
    private String childName;
    @Column(name = "nick_name")
    private String nickName;
    private Integer age;
    @Column(name = "email", unique = true)
    private String email;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Data dob;
    @Column(name = "phone_no", unique = true)
    private String phoneNo;

    //one more column need to add generic
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Address> addresses = new ArrayList<>();

    public String toString(){
        return JsonUtils.jsonOf(this);
    }

}
