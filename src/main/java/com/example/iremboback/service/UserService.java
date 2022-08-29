package com.example.iremboback.service;

import com.example.iremboback.model.Users;

import java.util.List;
import java.util.Optional;

public interface UserService {

     Optional<Users> create(Users user);

     Optional<Users> createAdmin(Users user);

     Optional<Users> update(Users user);

     boolean delete(String email);

     List<Users> getUsers();

     Optional<Users> getUser(String email);

     Optional<Users> auth(String email, String password);
}
