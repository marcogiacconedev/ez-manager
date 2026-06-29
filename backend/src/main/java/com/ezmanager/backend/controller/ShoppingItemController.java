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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.dto.CreateShoppingItemRequest;
import com.ezmanager.backend.dto.ShoppingItemResponse;
import com.ezmanager.backend.dto.ShoppingListItemResponse;
import com.ezmanager.backend.dto.UpdateShoppingItemRequest;
import com.ezmanager.backend.service.ShoppingItemService;
import com.ezmanager.backend.service.ShoppingListItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shoppingitems")
public class ShoppingItemController {
    private final ShoppingItemService shoppingItemService;
    private final ShoppingListItemService shoppingListItemService;

    public ShoppingItemController(
        ShoppingItemService shoppingItemService,
        ShoppingListItemService shoppingListItemService
    ) {
        this.shoppingItemService = shoppingItemService;
        this.shoppingListItemService = shoppingListItemService;
    }

    @GetMapping("/{shoppingListId}/items")
    public ResponseEntity<Page<ShoppingListItemResponse>> getShoppingItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        Page<ShoppingListItemResponse> items = shoppingItemService.getShoppingItemsByShoppingListId(page, size, shoppingListId);
        return ResponseEntity.ok(items);
    }

    @DeleteMapping("/{shoppingListId}/items")
    public ResponseEntity<Void> deleteAllItemsInList(
        @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        shoppingListItemService.deleteAllItemsInList(shoppingListId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<ShoppingItemResponse> createShoppingItem(
        @Valid @RequestBody CreateShoppingItemRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingItemResponse created = shoppingItemService.createShoppingItem(dto, UUID.fromString(userId));
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

    @PutMapping("/{shoppingItemId}")
    public ResponseEntity<ShoppingItemResponse> updateShoppingItem(
        @Valid @RequestBody UpdateShoppingItemRequest dto,
        @PathVariable UUID shoppingItemId,
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.ok(shoppingItemService.updateShoppingItem(dto, shoppingItemId, UUID.fromString(userId)));        
    }
}
