package com.example.iremboback.controller;

import com.example.iremboback.config.security.JWTUtil;
import com.example.iremboback.dto.ApiError;
import com.example.iremboback.dto.ApiRequest;
import com.example.iremboback.dto.ApiResponse;
import com.example.iremboback.dto.ApiSuccess;
import com.example.iremboback.model.Users;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class LoginController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtil jwtUtil;

    @Autowired
    private UserService userService;

    private ApiError error;
    private ApiResponse response;
    private ApiSuccess success;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> verifyUser(@RequestBody ApiRequest request) {
        response = new ApiResponse();
        error = new ApiError();
        success = new ApiSuccess();
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
        System.out.println("Principal: " + authentication.getName());
        Users users = userService.getUser(authentication.getName()).get();
        if (users == null) {
            error.setErrorCode(403);
            error.setErrorMessage("Unauthorized Access");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
}
