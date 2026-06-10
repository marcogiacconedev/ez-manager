package com.ezmanager.backend.model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "Shopping_Items")
public class ShoppingItem {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(
        nullable = false,
        name = "name"
    )
    private String name;

    @Column(
        name = "category"
    )
    private String category;

    @Column(
        nullable = false,
        name = "price"
    )
    private Float price;

    @Column(
        nullable = false,
        name = "quantity"
    )
    private Integer quantity;

    @Column(
        nullable = false,
        name = "added"
    )
    private Boolean added;

    @Column(
        nullable = false,
        name = "shopping_list_id"
    )
    private UUID shoppingListId;

    @Column(
        nullable = false,
        name = "created_at"
    )
    private LocalDateTime createdAt;

    //getters and setters
    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }

    public Float getPrice() {
        return price;
    }
    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }
    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getAdded() {
        return added;
    }
    public void setAdded(Boolean added) {
        this.added = added;
    }

    public UUID getShoppingListId() {
        return shoppingListId;
    }
    public void setShoppingListId(UUID shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
