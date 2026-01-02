package com.ezmanager.backend.dto;

import java.util.UUID;

import com.ezmanager.backend.model.User;

public class UserResponse {

    private UUID id;
    private String userName;
    private String userEmail;
    private String role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.userName = user.getName();
        this.userEmail = user.getEmail();
        this.role = user.getRole();
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getRole() {
        return role;
    }

    
}
