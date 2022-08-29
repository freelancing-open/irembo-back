package com.example.iremboback.service.Implementation;

import com.example.iremboback.model.Role;
import com.example.iremboback.repository.RoleRepository;
import com.example.iremboback.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleDB;

    @Override
    public Optional<Role> create(Role role) {
        return Optional.of(roleDB.save(role));
    }

    @Override
    public List<Role> getAllRoles() {
        return roleDB.findAll();
    }
}
