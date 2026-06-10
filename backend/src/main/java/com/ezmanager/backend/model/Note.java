package com.ezmanager.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Notes")
public class Note {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(
        nullable = false,
        name = "user_id"
    )
    private UUID userId;

    @Column(
        nullable = false,
        name = "name"
    )
    private String name;

    @Column(
        nullable = false,
        name = "text"
    )
    private String text;

    @Column(
        nullable = false,
        name = "created_at"
    )
    private LocalDateTime createdAt;


    //getters and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }
    public void setText(String text) {
        this.text = text;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
