package com.example.iremboback.validation;

import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailValidation {

    @Autowired
    private UserService userService;

    public boolean isAlreadyRegister(String emailAddress) {
        return userService.getUser(emailAddress).isPresent();
    }
}
