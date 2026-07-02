package com.ezmanager.backend.dto;

import java.util.UUID;

import com.ezmanager.backend.model.ShoppingItem;
import com.ezmanager.backend.model.ShoppingList;
import com.ezmanager.backend.model.ShoppingListItem;

public class ShoppingListItemResponse {
    private UUID id;
    private UUID shoppingListId;
    private UUID shoppingItemId;
    private Integer quantity;
    private Boolean added;
    private String itemName;
    private String listName;
    private Float price;
    private String category;

    public ShoppingListItemResponse(ShoppingListItem shoppingListItem) {
        ShoppingItem item = shoppingListItem.getShoppingItem();
        ShoppingList list = shoppingListItem.getShoppingList();
        this.id = shoppingListItem.getId();
        this.shoppingListId = list.getId();
        this.listName = list.getName();
        this.shoppingItemId = item.getId();
        this.itemName = item.getName();
        this.quantity = shoppingListItem.getQuantity();
        this.added = shoppingListItem.getAdded();
        this.price = item.getPrice();
        this.category = item.getCategory();
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

    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getListName() { return listName; }
    public void setListName(String listName) { this.listName = listName; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}