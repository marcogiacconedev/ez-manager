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

    @Column(
        nullable = false,
        name = "user_name"
    )
    private String userName;

    @Column(
        nullable = false,
        name = "user_email"
    )
    private String userEmail;

    @Column(
        nullable = false,
        name = "pass_hash"
    )
    private String passHash;

    @Column(
        nullable = false,
        name = "user_role"
    )
    private final String userRole = "USER";

    public UUID getId() { return this.id; }
    public String getName() { return this.userName; }
    public String getEmail() { return this.userEmail; }
    public String getRole() { return this.userRole; }

}
