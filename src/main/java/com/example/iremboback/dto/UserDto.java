package com.example.iremboback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserDto {

    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private Date dob;
    private String maritalStatus;
    private String nationality;
    private String profilePic;
}
