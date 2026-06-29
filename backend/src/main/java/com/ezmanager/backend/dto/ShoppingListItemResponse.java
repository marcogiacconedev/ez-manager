package com.ezmanager.backend.dto;

import java.util.UUID;

import com.ezmanager.backend.model.ShoppingListItem;

public class ShoppingListItemResponse {
    private UUID id;
    private UUID shoppingListId;
    private UUID shoppingItemId;
    private Integer quantity;
    private Boolean added;

    public ShoppingListItemResponse(ShoppingListItem entity) {
        this.id = entity.getId();
        this.shoppingListId = entity.getShoppingList().getId();
        this.shoppingItemId = entity.getShoppingItem().getId();
        this.quantity = entity.getQuantity();
        this.added = entity.getAdded();
    }

    // getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getShoppingListId() { return shoppingListId; }
    public void setShoppingListId(UUID shoppingListId) { this.shoppingListId = shoppingListId; }

    public UUID getShoppingItemId() { return shoppingItemId; }
    public void setShoppingItemId(UUID shoppingItemId) { this.shoppingItemId = shoppingItemId; }

    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }

    public Boolean getAdded() { return added; }
    public void setAdded(Boolean added) { this.added = added; }
}