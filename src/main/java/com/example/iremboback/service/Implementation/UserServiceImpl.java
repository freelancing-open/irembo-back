package com.example.iremboback.service.Implementation;

import com.example.iremboback.model.Users;
import com.example.iremboback.repository.UserRepository;
import com.example.iremboback.service.RoleService;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDB;

    @Autowired
    private RoleService roleService;


    @Override
    public Optional<Users> create(Users user) {
        user.setRoleName(roleService.getAllRoles().get(0));
        user.setVerified(false);
        return Optional.of(userDB.save(user));
    }

    @Override
    public Optional<Users> createAdmin(Users user) {
        user.setRoleName(roleService.getAllRoles().get(1));
        user.setVerified(true);
        return Optional.of(userDB.save(user));
    }

    @Override
    public Optional<Users> update(Users user) {
        return Optional.of(userDB.save(user));
    }

    @Override
    public boolean delete(String email) {
        Optional<Users> toDelete = getUser(email);
        if(toDelete.isEmpty()) return false;
        userDB.deleteById(toDelete.get().getId());
        return true;
    }

    @Override
    public List<Users> getUsers() {
        return userDB.findAll();
    }

    @Override
    public Optional<Users> getUser(String email) {
        Optional<Users> user = userDB.findAll().stream().filter(u -> u.getEmail().equals(email)).findFirst();
        if(user.isPresent()) return user;
        return Optional.empty();
    }
}
