package com.example.iremboback.config.security;

import com.example.iremboback.model.Users;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        Optional<Users> u = userService.getUser(user);
        u.orElseThrow(() -> new UsernameNotFoundException("Incorrect Credentials"));
        return new UsersDetail(u.get());
    }
}
