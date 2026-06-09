package com.ezmanager.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.dto.LoginRequest;
import com.ezmanager.backend.dto.LoginResponse;
import com.ezmanager.backend.service.AuthService;

@RestController
@RequestMapping
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> getLoginToken(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        LoginResponse loginResponse = new LoginResponse(username + password); 

        return ResponseEntity.ok(loginResponse);   
    } 
}
