package com.lixin.vehical_management.controller;

import com.lixin.vehical_management.constants.BaseResponse;
import com.lixin.vehical_management.constants.CommonMessage;
import com.lixin.vehical_management.dto.EmployeeRequest;
import com.lixin.vehical_management.dto.RefreshTokenRequest;
import com.lixin.vehical_management.service.EmployeeService;
import com.lixin.vehical_management.service.RefreshTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1")
@Tag(name = "User Management")
public class UserController {
    private final EmployeeService userService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/register")
    @Operation(summary = "User register", description = "Allows 2 roles: admin and user")
    public ResponseEntity<?> registerNewUser(@Valid @RequestBody EmployeeRequest userRequest) {
        return userService.registerUser(userRequest);
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        Map<String, String> token = refreshTokenService.refreshToken(refreshTokenRequest.getUserId(), refreshTokenRequest);
        return new ResponseEntity<>(new BaseResponse<>(CommonMessage.SUCCESS,token), HttpStatus.OK);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        refreshTokenService.logout(refreshTokenRequest.getUserId());
        return new ResponseEntity<>(new BaseResponse<>(CommonMessage.SUCCESS), HttpStatus.OK);
    }
}
