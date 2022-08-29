package com.example.iremboback.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "role")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String description;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "roleName")
    private List<Users> users;

    @Override
    public String toString() {
        return "Role {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '}';
    }
}
