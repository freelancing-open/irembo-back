package com.example.iremboback.controller;

import com.example.iremboback.config.security.JWTUtil;
import com.example.iremboback.dto.*;
import com.example.iremboback.model.Users;
import com.example.iremboback.service.UserService;
import com.example.iremboback.validation.EmailValidation;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailValidation emailValidation;

    private ApiError error;
    private ApiResponse response;
    private ApiSuccess success;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyUser(@RequestBody ApiRequest request) {
        init();
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPwd()));
            //user details ===>
            final String jwt = jwtUtil.createToken(request.getEmail());

            success.setCode(HttpStatus.OK.value());
            response.setApiSuccess(success);
            response.setData(jwt);

        } catch (AuthenticationException e) {
            error.setErrorCode(401);
            error.setErrorMessage("Incorrect Credentials");
            response.setApiError(error);
        } catch (Exception e) {
            error.setErrorCode(500);
            error.setErrorMessage("JWT Not Created");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(Authentication authentication) {
        error = new ApiError();
        UserDto userDto = new UserDto();
        Optional<Users> users = userService.getUser(authentication.getName());
        if (users.isEmpty()) {
            error.setErrorCode(403);
            error.setErrorMessage("Unauthorized Access");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        BeanUtils.copyProperties(users.get(), userDto);
        userDto.setRole(users.get().getRoleName().getName());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> createUser(@RequestBody Users user){
        init();
        if(emailValidation(user)){
            return ResponseEntity.status(HttpStatus.OK).body(response);
        }
        UserDto userDto = new UserDto();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Optional<Users> created = userService.create(user);
        if(created.isPresent()) {
            BeanUtils.copyProperties(created.get(), userDto);
            userDto.setRole(created.get().getRoleName().getName());
            success.setCode(HttpStatus.CREATED.value());
            response.setApiSuccess(success);
            response.setData(userDto);
        }else{
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Not Implemented");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void init(){
        error = new ApiError();
        response = new ApiResponse();
        success = new ApiSuccess();
    }

    private boolean emailValidation(Users u){
        boolean validationResult = emailValidation.isAlreadyRegister(u.getEmail());
        if(validationResult){
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Email Already Exist");
            response.setApiError(error);
            return true;
        }
        return false;
    }
}
