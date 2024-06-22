package com.neonlab.common.enums;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.neonlab.common.expectations.InvalidInputException;
import lombok.Getter;



@Getter
public enum Gender {
    BOY("boy"),
    GIRL("girl");
    private final String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    @JsonCreator
    public static Gender fromString(String inputStatus) throws InvalidInputException {
        for (Gender gender1 : Gender.values()) {
            if (gender1.gender.equalsIgnoreCase(inputStatus)) {
                return gender1;
            }
        }
        throw new InvalidInputException("Unknown Status: " + inputStatus);
    }
}
