package com.example.iremboback.service;

import com.example.iremboback.model.Role;

import java.util.Optional;

public interface RoleService {

    Optional<Role> create(Role role);

}
