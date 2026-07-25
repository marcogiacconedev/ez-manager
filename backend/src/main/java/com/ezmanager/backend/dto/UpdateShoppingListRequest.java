package com.ezmanager.backend.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;

public class UpdateShoppingListRequest {
    @NotBlank(message = "Il nome della lista è obbligatorio")
    private String name;
    private LocalDateTime completedAt;
    private LocalDateTime createdAt;
    private String notes;
    private String status;

    //getters and setters
    public void setName(String name) { this.name = name; }
    public String getName() { return name; }
    
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt = completedAt; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    
    public void setNotes(String notes) { this.notes = notes; }
    public String getNotes() { return notes; }
    
    public void setStatus(String status) { this.status = status; }
    public String getStatus() { return status; }
    
}
