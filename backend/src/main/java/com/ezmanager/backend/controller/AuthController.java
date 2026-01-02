package com.ezmanager.backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.service.AuthService;

@RestController
@RequestMapping
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }
    
    // @PostMapping("/auth/login")
    // public ResponseEntity<String> getLoginToken(@RequestBody ) {
    //     token  = this.authService.
    // } 
}
