package com.ezmanager.backend.service;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.ShoppingItemResponse;
import com.ezmanager.backend.repository.ShoppingItemRepository;

@Service
public class ShoppingItemService {
    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingItemService(ShoppingItemRepository shoppingItemRepository) {
        this.shoppingItemRepository = shoppingItemRepository;
    }

    public Page<ShoppingItemResponse> getShoppingItemsByShoppingListId(int page, int size, UUID shoppingListId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return shoppingItemRepository.findByShoppingListId(shoppingListId, pageable).map(shoppingItem -> new ShoppingItemResponse(shoppingItem));
    }
    
}
