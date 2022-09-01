package com.example.iremboback.controller;

import com.example.iremboback.dto.ApiError;
import com.example.iremboback.dto.ApiResponse;
import com.example.iremboback.dto.ApiSuccess;
import com.example.iremboback.dto.UserDto;
import com.example.iremboback.model.Users;
import com.example.iremboback.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    private ApiError error;
    private ApiResponse response;
    private ApiSuccess success;

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(Authentication authentication) {
        init();
        if(authentication == null){
            error.setErrorCode(401);
            error.setErrorMessage("Login Again");
            response.setApiError(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        UserDto userDto = new UserDto();
        Optional<Users> users = userService.getUser(authentication.getName());
        if (users.isEmpty()) {
            error.setErrorCode(401);
            error.setErrorMessage("Users Doesn't Exist");
            response.setApiError(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
        BeanUtils.copyProperties(users.get(), userDto);
        userDto.setRole(users.get().getRoleName().getName());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }


    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> updateUser(@RequestBody Users user){
        init();
        UserDto userDto = new UserDto();
        Optional<Users> updated = userService.update(user);
        if(updated.isPresent()) {
            BeanUtils.copyProperties(updated.get(), userDto);
            userDto.setRole(updated.get().getRoleName().getName());
            success.setCode(HttpStatus.OK.value());
            response.setApiSuccess(success);
            response.setData(userDto);
        }else{
            error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
            error.setErrorMessage("Not Implemented");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(path = "/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getUser(@PathVariable String email){
        init();
        UserDto userDto = new UserDto();
        Optional<Users> user = userService.getUser(email);
        if(user.isPresent()) {
            BeanUtils.copyProperties(user.get(), userDto);
            userDto.setRole(user.get().getRoleName().getName());
            success.setCode(HttpStatus.OK.value());
            response.setApiSuccess(success);
            response.setData(userDto);
        }else{
            error.setErrorCode(HttpStatus.NOT_FOUND.value());
            error.setErrorMessage("Not Found");
            response.setApiError(error);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllUsers(){
        init();
        List<Users> users = userService.getUsers();
        success.setCode(HttpStatus.OK.value());
        response.setApiSuccess(success);
        response.setData(users);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void init(){
        error = new ApiError();
        response = new ApiResponse();
        success = new ApiSuccess();
    }
}
