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

    // --- usato da AuthService ---

    public User findByUsername(String username) {
        return userRepository.findByUserName(username)
                .orElse(null);
    }

    public boolean existsByUsername(String username) {
        return userRepository.existsByUserName(username);
    }

    public boolean existsByEmail(String email) {
        return userRepository.existsByUserEmail(email);
    }

    public User createUser(String username, String email, String passHash) {
        User user = new User();
        user.setUserName(username);
        user.setUserEmail(email);
        user.setPassHash(passHash);
        return userRepository.save(user);
    }
}
