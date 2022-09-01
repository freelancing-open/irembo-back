package com.example.iremboback.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "account_verification")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class AccountVerification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String identification;
    private String documentLink;
    private Account_Status status;

    public AccountVerification(String identification, String documentLink, Account_Status status, Users u) {
        this.identification = identification;
        this.documentLink = documentLink;
        this.status = status;
        this.users = u;
    }

    @JsonIgnore
    @OneToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "users_id", referencedColumnName = "id")
    private Users users;
}
