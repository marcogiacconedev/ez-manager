package com.ezmanager.backend.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ezmanager.backend.dto.ShoppingListItemRequest;
import com.ezmanager.backend.dto.ShoppingListItemResponse;
import com.ezmanager.backend.dto.UpdateShoppingListItemRequest;
import com.ezmanager.backend.model.ShoppingItem;
import com.ezmanager.backend.model.ShoppingList;
import com.ezmanager.backend.model.ShoppingListItem;
import com.ezmanager.backend.repository.ShoppingItemRepository;
import com.ezmanager.backend.repository.ShoppingListItemRepository;
import com.ezmanager.backend.repository.ShoppingListRepository;


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

    public List<ShoppingListItemResponse> syncList(List<ShoppingListItemRequest> dtoList, UUID listId, UUID userId) {
        //prende la lista
        ShoppingList shoppingList = shoppingListRepository.findById(listId).orElseThrow(() -> new RuntimeException("Lista non trovata"));
        if (!shoppingList.getUserId().equals(userId)) {
            throw new RuntimeException("Non autorizzato");
        }

        if (!dtoList.isEmpty()) {
            shoppingList.setName(dtoList.get(0).getListName());
            shoppingList.setUserId(userId);
            shoppingList.setCreatedAt(LocalDateTime.now());
            shoppingList.setStatus("PENDING");
            shoppingList.setNotes(dtoList.get(0).getNotes());
        }
        shoppingListRepository.save(shoppingList);

        List<ShoppingListItemResponse> responses = new ArrayList<>();

        // per ogni oggetto nel body
        dtoList.forEach(dto -> {
            // iniziamo dagli items: dal dto prendo l' id dell' item e lo uso per prendere l' item. 
            ShoppingItem shoppingItem = shoppingItemRepository.findById(dto.getShoppingItemId())
                .orElseThrow(() -> new RuntimeException("Item non trovato"));
            if (!shoppingItem.getUserId().equals(userId)) {
                throw new RuntimeException("Non autorizzato");
            }
            // sostituisco le uniche 2 prop che si possono modificare da FE
            shoppingItem.setName(dto.getItemName());
            shoppingItem.setPrice(dto.getPrice());

            // salvo
            shoppingItemRepository.save(shoppingItem);

            // shopping items lists: registriamo gli oggetti sulla lista
            ShoppingListItem shoppingListItem = new ShoppingListItem();
            if (dto.getId() != null) {
                shoppingListItem = shoppingListItemRepository.findById(dto.getId()).orElse(null);
            } else {
                shoppingListItem.setShoppingList(shoppingList);
                shoppingListItem.setShoppingItem(shoppingItem);
            }
            shoppingListItem.setAdded(dto.getAdded());
            shoppingListItem.setQuantity(dto.getQuantity());
            shoppingListItemRepository.save(shoppingListItem);
            
            //pushamo la response creata nella list 
            ShoppingListItemResponse shoppingListItemResponse = new ShoppingListItemResponse(shoppingListItem);
            responses.add(shoppingListItemResponse);
        });

        List<ShoppingListItem> shoppingListItems = shoppingListItemRepository.findByShoppingListId(listId);

        if (responses.isEmpty()) {
            // nessun item rimasto: cancella tutto in un colpo solo
            shoppingListItemRepository.deleteAllInBatch(shoppingListItems);
        } else {
            // cancella solo gli item non più presenti nella risposta
            List<ShoppingListItem> toDelete = shoppingListItems.stream()
                    .filter(item -> responses.stream()
                            .noneMatch(response -> item.getId().equals(response.getId())))
                    .collect(Collectors.toList());

            if (!toDelete.isEmpty()) {
                shoppingListItemRepository.deleteAllInBatch(toDelete); // batch anche qui, invece di N delete singole
            }
        }

        return responses;
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
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findByShoppingListIdAndShoppingItemId(shoppingListId, shoppingItemId).orElseThrow(() -> new RuntimeException("Item non nella lista"));
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new RuntimeException("lista non trovata"));
        if (!shoppingList.getUserId().equals(userId)) throw new RuntimeException("Non autorizzato");
        shoppingListItemRepository.delete(shoppingListItem);
    }

    public ShoppingListItemResponse updateShoppingListItem(
        UpdateShoppingListItemRequest dto,
        UUID shoppingListId,
        UUID shoppingItemId,
        UUID userId
    ) {
        ShoppingListItem shoppingListItem = shoppingListItemRepository.findByShoppingListIdAndShoppingItemId(shoppingListId, shoppingItemId).orElseThrow(() -> new RuntimeException("Item non nella lista"));
        ShoppingList shoppingList = shoppingListRepository.findById(shoppingListId).orElseThrow(() -> new RuntimeException("Lista inesistente"));
        if (!shoppingList.getUserId().equals(userId)) { throw new RuntimeException("Non autorizzato"); }

        shoppingListItem.setAdded(dto.getAdded());
        shoppingListItem.setQuantity(dto.getQuantity());
        
        return new ShoppingListItemResponse(shoppingListItemRepository.save(shoppingListItem));
    }
}
