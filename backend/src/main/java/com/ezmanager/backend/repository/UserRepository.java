package com.ezmanager.backend.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezmanager.backend.model.User;

public interface UserRepository extends JpaRepository<User, UUID>{
    
}
