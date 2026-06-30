package com.ezmanager.backend.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.AddItemToListRequest;
import com.ezmanager.backend.dto.ShoppingListItemResponse;
import com.ezmanager.backend.model.ShoppingItem;
import com.ezmanager.backend.model.ShoppingList;
import com.ezmanager.backend.model.ShoppingListItem;
import com.ezmanager.backend.repository.ShoppingItemRepository;
import com.ezmanager.backend.repository.ShoppingListItemRepository;
import com.ezmanager.backend.repository.ShoppingListRepository;

import tools.jackson.databind.ObjectMapper;

@Service
public class ShoppingListItemService {
    private final ShoppingListItemRepository shoppingListItemRepository;
    private final ShoppingListRepository shoppingListRepository;
    private final ShoppingItemRepository shoppingItemRepository;

    public ShoppingListItemService(
        ShoppingListItemRepository shoppingListItemRepository,
        ShoppingListRepository shoppingListRepository,
        ShoppingItemRepository shoppingItemRepository
    ) {
        this.shoppingListItemRepository = shoppingListItemRepository;
        this.shoppingListRepository = shoppingListRepository;
        this.shoppingItemRepository = shoppingItemRepository;
    }

    public ShoppingListItemResponse assignItemToList(AddItemToListRequest dto, UUID listId, UUID userId) {
        ShoppingList shoppingList = shoppingListRepository.findById(listId)
            .orElseThrow(() -> new RuntimeException("Lista non trovata"));

        ShoppingItem shoppingItem = shoppingItemRepository.findById(dto.getItemId())
            .orElseThrow(() -> new RuntimeException("Item non trovato"));

        if (!shoppingList.getUserId().equals(userId) || !shoppingItem.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato!");
        } 

        Boolean itemAlreadyInList = shoppingListItemRepository.existsByShoppingListIdAndShoppingItemId(listId, dto.getItemId()); 
        if (itemAlreadyInList) {
            throw new RuntimeException("L' item è gia in lista");
        }


        ShoppingListItem shoppingListItem = new ShoppingListItem();
        shoppingListItem.setShoppingList(shoppingList);
        shoppingListItem.setShoppingItem(shoppingItem);
        shoppingListItem.setAdded(false);
        shoppingListItem.setQuantity(dto.getQuantity());
        
        ObjectMapper mapper = new ObjectMapper();
        System.out.print(mapper.toString());
        return new ShoppingListItemResponse(shoppingListItemRepository.save(shoppingListItem));
    }

    public void deleteAllItemsInList(UUID shoppingListId, UUID userId) {
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new RuntimeException("Lista inesistente"));
        if (!shoppingList.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato");
        }
        List<ShoppingListItem> toBeDeleted = shoppingListItemRepository.findByShoppingListId(shoppingListId); 
        shoppingListItemRepository.deleteAll(toBeDeleted);
    }

    public void deleteItemFromList(UUID shoppingListId, UUID shoppingItemId, UUID userId) {
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findByShoppingListIdAndShoppingItemId(shoppingListId, shoppingItemId);
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new RuntimeException("lista non trovata"));
        if (!shoppingList.getUserId().equals(userId)) throw new RuntimeException("Non autorizzato");
        shoppingListItemRepository.delete(shoppingListItem);
    }
}
