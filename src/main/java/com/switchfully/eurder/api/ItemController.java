package com.switchfully.eurder.api;


import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "items")
public class ItemController {

    private final IItemService itemService;

    @Autowired
    public ItemController(IItemService itemService, ICustomerService customerService) {
        this.itemService = itemService;
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    ItemDTO createNewItem(@RequestBody CreateItemDTO createItemDTO, @RequestHeader String authorization) {
        return itemService.createNewItem(createItemDTO);
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    ItemDTO updateItem(@PathVariable UUID id, @RequestBody UpdateItemDTO updateItemDTO) {
        return itemService.updateItemById(updateItemDTO, id);
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/stock")
    List<ItemDTO> getItemsSortedByUrgency() {
        return itemService.getItemsSortedByUrgency();
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/stock/{urgency}")
    List<ItemDTO> getItemOnUrgency(@PathVariable String urgency) {
        return itemService.getItemsOnUrgency(urgency);
    }

}
