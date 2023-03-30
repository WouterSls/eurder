package com.switchfully.eurder.api;


import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "items")
public class ItemController {

    private final IItemService itemService;

    @Autowired

    public ItemController(IItemService itemService) {
        this.itemService = itemService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    List<ItemDTO> getAllItems(){
        return itemService.getListItemsDTO();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "/create", produces = "application/json", consumes = "application/json")
    ItemDTO createNewItem(@RequestBody CreateItemDTO createItemDTO){
        return itemService.createNewItem(createItemDTO);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}", produces = "application/json")
    ItemDTO getItemById(@PathVariable String id){
        return itemService.getItemById(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}/update",consumes = "application/json", produces = "application/json")
    ItemDTO updateItem(@PathVariable String id, @RequestBody UpdateItemDTO udpateItemDTO){
        //TODO:
        return null;
    }
}
