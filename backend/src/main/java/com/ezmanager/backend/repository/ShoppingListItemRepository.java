package com.ezmanager.backend.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezmanager.backend.model.ShoppingListItem;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, UUID> {

    // tutti gli item di una lista
    List<ShoppingListItem> findByShoppingListId(UUID shoppingListId);
}