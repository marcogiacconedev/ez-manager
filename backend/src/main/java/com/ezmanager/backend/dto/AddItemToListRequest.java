package com.ezmanager.backend.dto;

import java.util.UUID;

public class AddItemToListRequest {
    private UUID itemId;
    private Integer quantity;

    // getters and setters
    public UUID getItemId() { return itemId; }
    public void setItemId(UUID itemId) { this.itemId = itemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}