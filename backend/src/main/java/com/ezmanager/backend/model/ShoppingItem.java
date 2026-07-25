package com.ezmanager.backend.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "Shopping_Items")
public class ShoppingItem {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, name = "user_id")
    private UUID userId;

    @Column(nullable = false, name = "name")
    private String name;

    @Column(nullable = false, name = "category")
    private String category;

    @Column(nullable = true, name = "price")
    private Float price;

    @Column(nullable = true, name = "size")
    private Float size;

    @Column(nullable = true, name = "measure")
    private String measure;

    @OneToMany(mappedBy = "shoppingItem")
    private List<ShoppingListItem> listItems = new ArrayList<>();

    //getters and setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public Float getPrice() { return price; }
    public void setPrice(Float price) { this.price = price; }

    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public Float getSize() { return size; }
    public void setSize(Float size) { this.size = size; }
    
    public String getMeasure() { return measure; }
    public void setMeasure(String measure) { this.measure = measure; }
    
    public List<ShoppingListItem> getListItems() { return listItems; }
    public void setListItems(List<ShoppingListItem> listItems) { this.listItems = listItems; }
}
