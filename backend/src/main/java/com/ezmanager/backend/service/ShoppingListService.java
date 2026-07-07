package com.ezmanager.backend.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.CreateShoppingListRequest;
import com.ezmanager.backend.dto.ShoppingListResponse;
import com.ezmanager.backend.dto.UpdateShoppingListRequest;
import com.ezmanager.backend.model.ShoppingList;
import com.ezmanager.backend.repository.ShoppingListRepository;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public Page<ShoppingListResponse> getShoppingListsByUserId(UUID userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return shoppingListRepository.findByUserId(userId, pageable).map(shoppingList -> new ShoppingListResponse(shoppingList));
    }

    public ShoppingListResponse getShoppingListById(UUID shoppingListId, UUID userId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new RuntimeException("Lista non trovata!"));
        if (!shoppingList.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato");
        }

        return new ShoppingListResponse(shoppingList);
    }
    
    public ShoppingListResponse createShoppingList(CreateShoppingListRequest dto, UUID userId) {
        ShoppingList shoppingList = new ShoppingList();

        shoppingList.setUserId(userId);
        shoppingList.setCompletedAt(dto.getCompletedAt());
        shoppingList.setCreatedAt(LocalDateTime.now());
        shoppingList.setName(dto.getName());
        shoppingList.setNotes(dto.getNotes());
        shoppingList.setStatus(dto.getStatus());

        return new ShoppingListResponse(shoppingListRepository.save(shoppingList));
    }

    public void deleteShoppingList(UUID shoppingListId, UUID userId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new RuntimeException("Lista non trovata"));
        if (!shoppingList.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato");
        }
        shoppingListRepository.deleteById(shoppingListId);
    }

    public ShoppingListResponse updateShoppingList(UUID taskId, UpdateShoppingListRequest dto, UUID userId) {
        ShoppingList shoppingList = shoppingListRepository.findById(taskId).orElseThrow(() -> new RuntimeException("Task non trovata!"));
        
        if (!shoppingList.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato");
        }

        shoppingList.setName(dto.getName());
        shoppingList.setNotes(dto.getNotes());
        shoppingList.setStatus(dto.getStatus());
        shoppingList.setCompletedAt(dto.getCompletedAt());

        return new ShoppingListResponse(shoppingListRepository.save(shoppingList));
    }
}
