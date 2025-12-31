package com.ezmanager.backend.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.UserResponse;
import com.ezmanager.backend.model.User;
import com.ezmanager.backend.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse getUserById(UUID id) {
        User user = userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        return new UserResponse(user);
    }
    
}
