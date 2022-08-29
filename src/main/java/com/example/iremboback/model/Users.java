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
    @Column(name = "date-of-birth")
    private Date dob;
    private String maritalStatus;
    private String nationality;
    @Column(name = "profile-picture")
    private String profilePic;

    @JoinColumn(name = "roleName", referencedColumnName = "name")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private Role roleName;

}
