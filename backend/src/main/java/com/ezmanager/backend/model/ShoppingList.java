package com.ezmanager.backend.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Shopping_Lists")
public class ShoppingList {
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
        name = "notes"
    )
    private String notes;

    @Column(
        nullable = false,
        name = "status"
    )
    private String status;

    @Column(
        nullable = false,
        name = "created_at"
    )
    private String createdAt;

    @Column(
        name = "completed_at"
    )
    private String completedAt;

    //getters and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    
    public UUID getUserId() {
        return userId;
    }
    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }
    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }
}
