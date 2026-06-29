package com.ezmanager.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezmanager.backend.model.ShoppingItem;

public interface ShoppingItemRepository extends JpaRepository<ShoppingItem, UUID>{
    Page<ShoppingItem> findByUserId(UUID shoppingListId, Pageable pageable);
}
