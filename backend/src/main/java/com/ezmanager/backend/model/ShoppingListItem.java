package com.ezmanager.backend.model;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Shopping_Lists_Items")
public class ShoppingListItem {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(nullable = false, name = "shopping_list_id")
    private ShoppingList shoppingList;

    @ManyToOne  
    @JoinColumn(nullable = false, name = "shopping_item_id")
    private ShoppingItem shoppingItem;

    @Column(nullable = false, name = "quantity")
    private Integer quantity;

    @Column(nullable = false, name = "added")
    private Boolean added;

    public Boolean getAdded() { return added; }
    public void setAdded(Boolean added) { this.added = added; }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
    public ShoppingItem getShoppingItem() { return shoppingItem; }
    public void setShoppingItem(ShoppingItem shoppingItem) { this.shoppingItem = shoppingItem; }
    public ShoppingList getShoppingList() { return shoppingList; }
    public void setShoppingList(ShoppingList shoppingList) { this.shoppingList = shoppingList; }
}
