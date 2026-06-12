package com.ezmanager.backend.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.dto.CreateShoppingItemRequest;
import com.ezmanager.backend.dto.ShoppingItemResponse;
import com.ezmanager.backend.service.ShoppingItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shoppingitems")
public class ShoppingItemController {
    
    private final ShoppingItemService shoppingItemService;

    public ShoppingItemController(ShoppingItemService shoppingItemService) {
        this.shoppingItemService = shoppingItemService;
    }

    @GetMapping("/{shoppingListId}")
    public ResponseEntity<Page<ShoppingItemResponse>> getShoppingItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @Valid @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.ok(shoppingItemService.getShoppingItemsByShoppingListId(page, size, shoppingListId));
    }

    @PostMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingItemResponse> createShoppingItem(
        @Valid @RequestBody CreateShoppingItemRequest dto,
        @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingItemResponse created = shoppingItemService.createShoppingItem(dto, shoppingListId);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{shoppingItemId}")
    public ResponseEntity<Void> deleteShoppingItem(
        @PathVariable UUID shoppingItemId,
        @AuthenticationPrincipal String userId
    ) {
        shoppingItemService.deleteShoppingItem(shoppingItemId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }
}
