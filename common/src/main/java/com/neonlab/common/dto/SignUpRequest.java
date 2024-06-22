package com.neonlab.common.dto;
import lombok.Data;
import java.util.Date;


@Data
public class SignUpRequest {
    private String childName;
    private String nicName;
    private Integer age;
    private String email;
    private String gender;
    private String phoneNo;
    private Date dob;
}
