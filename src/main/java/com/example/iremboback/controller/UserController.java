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

    public void init(){
        error = new ApiError();
        response = new ApiResponse();
        success = new ApiSuccess();
    }
}
