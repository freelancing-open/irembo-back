package com.example.iremboback.controller;

import com.example.iremboback.dto.ApiError;
import com.example.iremboback.dto.ApiResponse;
import com.example.iremboback.dto.ApiSuccess;
import com.example.iremboback.dto.VerificationDto;
import com.example.iremboback.model.AccountVerification;
import com.example.iremboback.model.Users;
import com.example.iremboback.service.AccountVerificationService;
import com.example.iremboback.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountVerificationController {


    @Autowired
    private AccountVerificationService verificationService;

    @Autowired
    private UserService userService;

    private ApiError error;
    private ApiResponse response;
    private ApiSuccess success;

    @PutMapping(path = "/verify", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<?> updateUser(@RequestBody VerificationDto verificationDto){
        init();
        Optional<Users> user = userService.getUser(verificationDto.getEmail());
        if(user.isPresent()){
            Optional<AccountVerification> updated = verificationService.verify(verificationDto.getIdentification(), verificationDto.getDocLink(), user.get());
            if(updated.isPresent()) {
                success.setCode(HttpStatus.OK.value());
                response.setApiSuccess(success);
                response.setData(updated.get());
            }else{
                error.setErrorCode(HttpStatus.NOT_IMPLEMENTED.value());
                error.setErrorMessage("Not Implemented");
                response.setApiError(error);
            }
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void init(){
        error = new ApiError();
        response = new ApiResponse();
        success = new ApiSuccess();
    }

}
