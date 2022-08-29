package com.example.iremboback.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Anonymous
 * @version 1.0.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private ApiError apiError;
    private ApiSuccess apiSuccess;
    private Object data;
}
