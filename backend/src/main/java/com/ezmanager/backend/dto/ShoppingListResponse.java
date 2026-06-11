package com.ezmanager.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ezmanager.backend.model.ShoppingList;

public class ShoppingListResponse {
    private UUID id;
    private UUID userId;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    private String name;
    private String notes;
    private String status;

    public ShoppingListResponse(ShoppingList shoppingList) {
        this.id = shoppingList.getId();
        this.userId = shoppingList.getUserId();
        this.createdAt = shoppingList.getCreatedAt();
        this.completedAt = shoppingList.getCompletedAt();
        this.name = shoppingList.getName();
        this.notes = shoppingList.getNotes();
        this.status = shoppingList.getStatus();
    }

    //getters
    public UUID getId() { return id; }
    public LocalDateTime getCompletedAt() { return completedAt; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public String getName() { return name; }
    public String getNotes() { return notes; }
    public String getStatus() { return status; }
    public UUID getUserId() { return userId; }
}
