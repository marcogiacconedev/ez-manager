package com.ezmanager.backend.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezmanager.backend.dto.CreateShoppingListRequest;
import com.ezmanager.backend.dto.ShoppingListResponse;
import com.ezmanager.backend.service.ShoppingListService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/shoppinglists")
public class ShoppingListController {

    private final ShoppingListService shoppingListService;

    public ShoppingListController(ShoppingListService shoppingListService) {
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

    @PostMapping
    public ResponseEntity<ShoppingListResponse> createShoppingList(
        @Valid @RequestBody CreateShoppingListRequest dto,
        @AuthenticationPrincipal String userId
    ) {
        ShoppingListResponse created = shoppingListService.createShoppingList(dto, UUID.fromString(userId));
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
}
