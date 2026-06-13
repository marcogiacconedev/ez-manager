package com.ezmanager.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.CreateShoppingItemRequest;
import com.ezmanager.backend.dto.ShoppingItemResponse;
import com.ezmanager.backend.dto.UpdateShoppingItemRequest;
import com.ezmanager.backend.model.ShoppingItem;
import com.ezmanager.backend.model.ShoppingList;
import com.ezmanager.backend.repository.ShoppingItemRepository;
import com.ezmanager.backend.repository.ShoppingListRepository;

@Service
public class ShoppingItemService {
    private final ShoppingItemRepository shoppingItemRepository;
    private final ShoppingListRepository shoppingListRepository;

    public ShoppingItemService(
        ShoppingItemRepository shoppingItemRepository,
        ShoppingListRepository shoppingListRepository
    ) {
        this.shoppingItemRepository = shoppingItemRepository;
        this.shoppingListRepository = shoppingListRepository;
    }

    public Page<ShoppingItemResponse> getShoppingItemsByShoppingListId(int page, int size, UUID shoppingListId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return shoppingItemRepository.findByShoppingListId(shoppingListId, pageable).map(shoppingItem -> new ShoppingItemResponse(shoppingItem));
    }
    
    public ShoppingItemResponse createShoppingItem(CreateShoppingItemRequest dto, UUID shoppingListId) {
        ShoppingItem shoppingItem = new ShoppingItem();

        shoppingItem.setAdded(dto.getAdded());
        shoppingItem.setCategory(dto.getCategory());
        shoppingItem.setCreatedAt(LocalDateTime.now());
        shoppingItem.setName(dto.getName());
        shoppingItem.setPrice(dto.getPrice());
        shoppingItem.setQuantity(dto.getQuantity());
        shoppingItem.setShoppingListId(shoppingListId);

        return new ShoppingItemResponse(shoppingItemRepository.save(shoppingItem));
    }

    public void deleteShoppingItem(UUID shoppingItemId, UUID userId) {
        ShoppingItem shoppingItem = shoppingItemRepository.findById(shoppingItemId).orElseThrow(() -> new RuntimeException("Item non trovato"));
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingItem.getShoppingListId()).orElseThrow(() -> new RuntimeException("Shopping list non trovata"));
        if (shoppingList.getUserId().equals(userId)) {
            new RuntimeException("Non autorizzato");
        }

        shoppingItemRepository.delete(shoppingItem);
    }

    public ShoppingItemResponse updateShoppingItem(UpdateShoppingItemRequest dto, UUID shoppingItemId, UUID userId) {
        ShoppingItem shoppingItem = shoppingItemRepository.findById(shoppingItemId).orElseThrow(() -> new RuntimeException("Item non trovato!"));
        ShoppingList shoppingList = shoppingListRepository.findById(dto.getShoppingListId()).orElseThrow(() -> new RuntimeException("Lista non trovata!"));

        if (!shoppingList.getUserId().equals(userId)) {
            new RuntimeException("Non autorizzato!");
        }

        shoppingItem.setAdded(dto.getAdded());
        shoppingItem.setCategory(dto.getCategory());
        shoppingItem.setCreatedAt(shoppingItem.getCreatedAt());
        shoppingItem.setName(dto.getName());
        shoppingItem.setPrice(dto.getPrice());
        shoppingItem.setShoppingListId(dto.getShoppingListId());

        return new ShoppingItemResponse(shoppingItemRepository.save(shoppingItem));
    }
}
