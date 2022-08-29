package com.example.iremboback.config.security;

import com.example.iremboback.model.Users;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 *
 * @author Jack
 * @version 1.0.0
 */
@Service
public class UserInfo implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {
        Users u = userService.getUser(user).get();
        return new org.springframework.security.core.userdetails.User(u.getEmail(), u.getPassword(), new ArrayList<>());
    }
}
