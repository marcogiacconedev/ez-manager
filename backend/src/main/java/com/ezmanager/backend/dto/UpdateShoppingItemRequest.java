package com.ezmanager.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdateShoppingItemRequest {
    @NotBlank(message = "Il nome dell item è obbligatorio")
    private String name;
    private String category;
    private Float price;
    private Float size;
    private String measure;

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Float getSize() { return size; }
    public void setSize(Float size) { this.size = size; }
    public String getMeasure() { return measure; }
    public void setMeasure(String measure) { this.measure = measure; }
}
