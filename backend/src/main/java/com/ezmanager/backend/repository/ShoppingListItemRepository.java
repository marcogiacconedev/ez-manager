package com.ezmanager.backend.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ezmanager.backend.model.ShoppingListItem;

@Repository
public interface ShoppingListItemRepository extends JpaRepository<ShoppingListItem, UUID> {

    Page<ShoppingListItem> findByShoppingListId(UUID shoppingListId, Pageable pageable);
    List<ShoppingListItem> findByShoppingListId(UUID shoppingListId);
    Optional<ShoppingListItem> findByShoppingListIdAndShoppingItemId(UUID shoppingListId, UUID shoppingItemId);
    Boolean existsByShoppingListIdAndShoppingItemId(UUID shoppingListId, UUID shoppingItemId);
}