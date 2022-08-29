package com.example.iremboback.controller;

import com.example.iremboback.dto.ApiResponse;
import com.example.iremboback.dto.ApiSuccess;
import com.example.iremboback.model.Role;
import com.example.iremboback.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getAllRoles(){
        ApiSuccess success = new ApiSuccess();
        ApiResponse response = new ApiResponse();
        List<Role> roles = roleService.getAllRoles();
        success.setCode(HttpStatus.OK.value());
        response.setApiSuccess(success);
        response.setData(roles);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
