package com.ezmanager.backend.service;

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
import com.ezmanager.backend.repository.ShoppingItemRepository;


@Service
public class ShoppingItemService {
    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingItemService(
        ShoppingItemRepository shoppingItemRepository
    ) {
        this.shoppingItemRepository = shoppingItemRepository;
    }

    public Page<ShoppingItemResponse> getShoppingItemsByShoppingListId(int page, int size, UUID userId) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return shoppingItemRepository.findByUserId(userId, pageable).map(shoppingItem -> new ShoppingItemResponse(shoppingItem));
    }
    
    public ShoppingItemResponse createShoppingItem(CreateShoppingItemRequest dto, UUID userId) {
        ShoppingItem shoppingItem = new ShoppingItem();

        shoppingItem.setUserId(userId);
        shoppingItem.setCategory(dto.getCategory());
        shoppingItem.setName(dto.getName());
        shoppingItem.setPrice(dto.getPrice());
        shoppingItem.setSize(dto.getSize());
        shoppingItem.setMeasure(dto.getMeasure());

        return new ShoppingItemResponse(shoppingItemRepository.save(shoppingItem));
    }

    public void deleteShoppingItem(UUID shoppingItemId, UUID userId) {
        ShoppingItem shoppingItem = shoppingItemRepository.findById(shoppingItemId).orElseThrow(() -> new RuntimeException("Item non trovato"));
        if (shoppingItem.getUserId().equals(userId)) {
            new RuntimeException("Non autorizzato");
        }

        shoppingItemRepository.delete(shoppingItem);
    }

    public ShoppingItemResponse updateShoppingItem(UpdateShoppingItemRequest dto, UUID shoppingItemId, UUID userId) {
        ShoppingItem shoppingItem = shoppingItemRepository.findById(shoppingItemId).orElseThrow(() -> new RuntimeException("Item non trovato!"));

        if (!shoppingItem.getUserId().equals(userId)) {
            new RuntimeException("Non autorizzato!");
        }

        shoppingItem.setCategory(dto.getCategory());
        shoppingItem.setName(dto.getName());
        shoppingItem.setPrice(dto.getPrice());
        shoppingItem.setSize(dto.getSize());
        shoppingItem.setMeasure(dto.getMeasure());

        return new ShoppingItemResponse(shoppingItemRepository.save(shoppingItem));
    }
}
