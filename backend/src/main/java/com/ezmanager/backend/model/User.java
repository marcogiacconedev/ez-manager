package com.ezmanager.backend.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Users")
public class User {
    
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String user_name;

    @Column(nullable = false)
    private String user_email;

    @Column(nullable = false)
    private String pass_hash;

    @Column(nullable = false)
    private final String role = "USER";

    public UUID getId() { return this.id; }
    public String getName() { return this. user_name; }
    public String getEmail() { return this.user_email; }
    public String getRole() { return this.role; }

}
