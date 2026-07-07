package com.ezmanager.backend.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezmanager.backend.model.ShoppingList;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, UUID>{
    Page<ShoppingList> findByUserId(UUID userId, Pageable pageable);
}
