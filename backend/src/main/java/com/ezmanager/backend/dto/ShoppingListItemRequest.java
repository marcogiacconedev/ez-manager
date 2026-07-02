package com.ezmanager.backend.dto;

import java.util.UUID;

public class ShoppingListItemRequest {
    private UUID id;
    private UUID shoppingListId;
    private UUID shoppingItemId;
    private Integer quantity;
    private Boolean added;
    private String itemName;
    private String listName;
    private Float price;
    private String category;
    private String measure;
    private Float size;

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

    public String getMeasure() { return measure; }
    public void setMeasure(String measure) { this.measure = measure; }

    public Float getSize() { return size; }
    public void setSize(Float size) { this.size = size; }

    @Override
    public String toString() {
        return "ShoppingListItemRequest{" +
                "id=" + id +
                ", shoppingListId=" + shoppingListId +
                ", shoppingItemId=" + shoppingItemId +
                ", quantity=" + quantity +
                ", added=" + added +
                ", itemName='" + itemName + '\'' +
                ", listName='" + listName + '\'' +
                ", price=" + price +
                ", category='" + category + '\'' +
                '}';
    }
}
