package com.ezmanager.backend.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.ezmanager.backend.model.ShoppingItem;

public class ShoppingItemResponse {
    private UUID id;
    private Boolean added;
    private String category;
    private LocalDateTime createdAt;
    private String name;
    private Float price;
    private int quantity;
    private UUID shoppingListId;

    public ShoppingItemResponse(ShoppingItem shoppingItem) {
        this.id = shoppingItem.getId();
        this.added = shoppingItem.getAdded();
        this.category = shoppingItem.getCategory();
        this.createdAt = shoppingItem.getCreatedAt();
        this.name = shoppingItem.getName();
        this.price = shoppingItem.getPrice();
        this.quantity = shoppingItem.getQuantity();
        this.shoppingListId = shoppingItem.getShoppingListId();
    }

    public Boolean getAdded() { return added; }
    public String getCategory() { return category; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public UUID getId() { return id; }
    public String getName() { return name; }
    public Float getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public UUID getShoppingListId() { return shoppingListId; }
}
