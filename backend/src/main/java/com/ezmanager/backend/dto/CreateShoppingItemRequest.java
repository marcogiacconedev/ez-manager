package com.ezmanager.backend.dto;


public class CreateShoppingItemRequest {
    private String category;
    private String name;
    private Float price;
    private Float size;
    private String measure;

    //getters and setters
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }
    public String getMeasure() { return measure; }
    public void setMeasure(String measure) { this.measure = measure; }
    public Float getSize() { return size; }
    public void setSize(Float size) { this.size = size; }
}
