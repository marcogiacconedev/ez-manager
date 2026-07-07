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

import com.ezmanager.backend.dto.CreateShoppingListRequest;
import com.ezmanager.backend.dto.ShoppingListResponse;
import com.ezmanager.backend.dto.UpdateShoppingListRequest;
import com.ezmanager.backend.service.ShoppingListItemService;
import com.ezmanager.backend.service.ShoppingListService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shoppinglists")
public class ShoppingListController {
    private final ShoppingListService shoppingListService;

    public ShoppingListController(
        ShoppingListService shoppingListService, 
        ShoppingListItemService shoppingListItemService
    ) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping
    public ResponseEntity<Page<ShoppingListResponse>> getShoppingLists(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.ok(shoppingListService.getShoppingListsByUserId(UUID.fromString(userId), page, size));
    }

    @GetMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingListResponse> getShoppingListById(
        @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingListResponse shoppingListResponse = shoppingListService.getShoppingListById(shoppingListId, UUID.fromString(userId));
        return ResponseEntity.ok(shoppingListResponse); 
    }

    @PostMapping
    public ResponseEntity<ShoppingListResponse> createShoppingList(
        @Valid @RequestBody CreateShoppingListRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingListResponse created = shoppingListService.createShoppingList(dto, UUID.fromString(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @DeleteMapping("/{shoppingListId}")
    public ResponseEntity<Void> deleteShoppingList(
        @Valid @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        shoppingListService.deleteShoppingList(shoppingListId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{shoppingListId}")
    public ResponseEntity<ShoppingListResponse> updateShoppingList(
        @PathVariable UUID shoppingListId,
        @Valid @RequestBody UpdateShoppingListRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        return ResponseEntity.ok(shoppingListService.updateShoppingList(shoppingListId, dto, UUID.fromString(userId)));
    }
}
