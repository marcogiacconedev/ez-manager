package com.ezmanager.backend.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.dto.LoginRequest;
import com.ezmanager.backend.dto.LoginResponse;
import com.ezmanager.backend.dto.SignupRequest;
import com.ezmanager.backend.dto.UserResponse;
import com.ezmanager.backend.service.AuthService;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("signup")
    public ResponseEntity<UserResponse> signup(@RequestBody SignupRequest request) {
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    // logout no-op: il client cestina il token, il server non ha stato da pulire.
    @PostMapping("logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
