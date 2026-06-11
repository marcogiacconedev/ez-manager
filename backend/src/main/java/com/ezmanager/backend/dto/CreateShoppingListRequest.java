package com.ezmanager.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class CreateShoppingListRequest {
    @NotBlank(message = "Il nome della lista è obbligatorio")
    private String name;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private String status;
    private String notes;

    //getters and setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
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
}
