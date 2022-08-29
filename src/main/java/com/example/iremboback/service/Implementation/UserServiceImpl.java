package com.example.iremboback.service.Implementation;

import com.example.iremboback.model.Users;
import com.example.iremboback.repository.UserRepository;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userDB;

    @Override
    public Optional<Users> auth(String email, String password) {
        Optional<Users> authUser = userDB.findAll().stream().filter(u -> u.getEmail().equals(email) && u.getPassword().equals(password)).findFirst();
        if(authUser.isPresent()) return authUser;
        return Optional.empty();
    }

    @Override
    public Optional<Users> create(Users user) {
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
