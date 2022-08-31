package com.example.iremboback.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String firstName;
    private String lastName;
    @Column(unique = true, nullable = false)
    private String email;
    private String password;
    private String gender;
    @Column(name = "date_of_birth")
    private Date dob;
    private String maritalStatus;
    private String nationality;
    @Column(name = "profile_picture")
    private String profilePic;

    private Integer otp; //For Multi-Factor Authentication Code
    private boolean verified; // For Registration verification
    private String verificationCode;  // For Registration verification code
    private boolean tokenVerified;  // For Reset Password token verification
    private String token;  // For Reset password verification

    @JoinColumn(name = "roleName", referencedColumnName = "name")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Role roleName;

    public Users(String firstName, String lastName, String email, String password, String gender, Date dob, String maritalStatus, String nationality, String profilePic) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.gender = gender;
        this.dob = dob;
        this.maritalStatus = maritalStatus;
        this.nationality = nationality;
        this.profilePic = profilePic;
    }
}
