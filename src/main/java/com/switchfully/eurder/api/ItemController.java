package com.switchfully.eurder.api;


import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.utils.Feature;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(value = "items")
public class ItemController {

    private final IItemService itemService;
    private final ICustomerService customerService;

    @Autowired
    public ItemController(IItemService itemService, ICustomerService customerService) {
        this.itemService = itemService;
        this.customerService = customerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ItemDTO createNewItem(@RequestBody CreateItemDTO createItemDTO, @RequestHeader String authorization) {
        customerService.validateAuthorization(authorization, Feature.CREATE_NEW_ITEM);
        return itemService.createNewItem(createItemDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/update", consumes = "application/json", produces = "application/json")
    ItemDTO updateItem(@PathVariable UUID id, @RequestBody UpdateItemDTO updateItemDTO, @RequestHeader String authorization) {
        customerService.validateAuthorization(authorization, Feature.UPDATE_ITEM);
        return itemService.updateItemById(updateItemDTO, id);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/stock")
    List<ItemDTO> getItemsSortedByUrgency(@RequestHeader String authorization) {
        customerService.validateAuthorization(authorization, Feature.GET_ITEMS);
        return itemService.getItemsSortedByUrgency();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/stock/{urgency}")
    List<ItemDTO> getItemOnUrgency(@PathVariable String urgency, @RequestHeader String authorization) {
        customerService.validateAuthorization(authorization, Feature.GET_ITEMS_ON_URGENCY);
        return itemService.getItemsOnUrgency(urgency);
    }

}
