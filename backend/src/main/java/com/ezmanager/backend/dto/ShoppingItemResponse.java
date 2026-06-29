package com.ezmanager.backend.dto;

import java.util.UUID;

import com.ezmanager.backend.model.ShoppingItem;

public class ShoppingItemResponse {
    private UUID id;
    private UUID userId;
    private String category;
    private String name;
    private Float price;

    public ShoppingItemResponse(ShoppingItem shoppingItem) {
        this.id = shoppingItem.getId(); 
        this.userId = shoppingItem.getUserId();
        this.category = shoppingItem.getCategory(); 
        this.name = shoppingItem.getName();
        this.price = shoppingItem.getPrice();   
    }

    public UUID getId() { return id; }
    public UUID getUserId() { return userId; }
    public String getCategory() { return category; }
    public String getName() { return name; }
    public Float getPrice() { return price; }
}
