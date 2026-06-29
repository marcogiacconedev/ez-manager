package com.ezmanager.backend.dto;


public class CreateShoppingItemRequest {
    private String category;
    private String name;
    private Float price;

    //getters and setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
}
