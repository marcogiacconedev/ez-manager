package com.ezmanager.backend.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
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

import com.ezmanager.backend.dto.AddItemToListRequest;
import com.ezmanager.backend.dto.ShoppingListItemResponse;
import com.ezmanager.backend.dto.UpdateShoppingListItemRequest;
import com.ezmanager.backend.service.ShoppingItemService;
import com.ezmanager.backend.service.ShoppingListItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shoppinglistitems")
public class ShoppingListItemController {

    private final ShoppingListItemService shoppingListItemService;
    private final ShoppingItemService shoppingItemService;


    public ShoppingListItemController(
        ShoppingListItemService shoppingListItemService,
        ShoppingItemService shoppingItemService
    ) {
        this.shoppingListItemService = shoppingListItemService;
        this.shoppingItemService = shoppingItemService;
    }

    @GetMapping("/{shoppingListId}/items")
    public ResponseEntity<Page<ShoppingListItemResponse>> getShoppingListItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        Page<ShoppingListItemResponse> items = shoppingItemService.getShoppingItemsByShoppingListId(page, size, shoppingListId);
        return ResponseEntity.ok(items);
    }

    @PostMapping("/{shoppingListId}/items")
    public ResponseEntity<ShoppingListItemResponse> addItemToList(
        @PathVariable UUID shoppingListId,
        @Valid @RequestBody AddItemToListRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingListItemResponse created = shoppingListItemService.assignItemToList(dto, shoppingListId, UUID.fromString(userId));
        return ResponseEntity.ok(created);
    }

    @DeleteMapping("/{shoppingListId}/items")
    public ResponseEntity<Void> deleteAllItemsInList(
        @PathVariable UUID shoppingListId,
        @AuthenticationPrincipal String userId
    ) {
        shoppingListItemService.deleteAllItemsInList(shoppingListId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{shoppingListId}/items/{shoppingItemId}")
    public ResponseEntity<Void> deleteItemFromList(
        @PathVariable UUID shoppingListId,
        @PathVariable UUID shoppingItemId,
        @AuthenticationPrincipal String userId
    ) {
        shoppingListItemService.deleteItemFromList(shoppingListId, shoppingItemId, UUID.fromString(userId));
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{shoppingListId}/items/{shoppingItemId}")
    public ResponseEntity<ShoppingListItemResponse> updateShoppingListItem(
        @PathVariable UUID shoppingListId,
        @PathVariable UUID shoppingItemId,
        @RequestBody UpdateShoppingListItemRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingListItemResponse updatedItem = shoppingListItemService.updateShoppingListItem(dto, shoppingListId, shoppingItemId, UUID.fromString(userId));
        return ResponseEntity.ok(updatedItem);
    }
}
