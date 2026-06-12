package com.ezmanager.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class CreateShoppingItemRequest {
    private Boolean added;
    private String category;
    private LocalDateTime createdAt;
    private String name;
    private Float price;
    private int quantity;
    private UUID shoppingListId;

    //getters and setters
    public Boolean getAdded() { return added; }
    public void setAdded(Boolean added) { this.added = added; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public UUID getShoppingListId() { return shoppingListId; }
    public void setShoppingListId(UUID shoppingListId) { this.shoppingListId = shoppingListId; }

}
