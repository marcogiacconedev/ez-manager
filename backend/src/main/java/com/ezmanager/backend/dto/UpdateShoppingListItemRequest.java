package com.ezmanager.backend.dto;

public class UpdateShoppingListItemRequest {
    private Boolean added;
    private Integer quantity;
    
    public Boolean getAdded() { return added; }
    public void setAdded(Boolean added) { this.added = added; }
    public Integer getQuantity() { return quantity; }
    public void setQuantity(Integer quantity) { this.quantity = quantity; }
}
