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
    private String email;
    private String password;
    private String gender;
    private Date dob;
    private String maritalStatus;
    private String nationality;
    private String profilePic;

    @JoinColumn(name = "roleName", referencedColumnName = "name")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Role roleName;

}
