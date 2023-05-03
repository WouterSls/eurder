package com.switchfully.eurder.api;


import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.components.itemComponent.ItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "items")
public class ItemController {

    private final IItemService itemService;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemController(IItemService itemService, ItemMapper itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(produces = "application/json", consumes = "application/json")
    ItemDTO createNewItem(@RequestBody CreateItemDTO createItemDTO) {
        return itemMapper.mapToDTO(itemService.createNewItem(createItemDTO));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces =  "application/json")
    List<ItemDTO> findAllItems(){
        return itemMapper.mapToDTO(itemService.findAllItems());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping(path = "/{id}", consumes = "application/json", produces = "application/json")
    ItemDTO updateItem(@PathVariable UUID id, @RequestBody UpdateItemDTO updateItemDTO) {
        return itemMapper.mapToDTO(itemService.updateItemById(updateItemDTO, id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/{id}")
    ItemDTO getItemById(@PathVariable UUID id){
        return itemMapper.mapToDTO(itemService.findById(id));
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/stock")
    List<ItemDTO> getItemsSortedByUrgency() {
        return itemMapper.mapToDTO(itemService.getItemsSortedByUrgency());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json", path = "/stock/{urgency}")
    List<ItemDTO> getItemOnUrgency(@PathVariable String urgency) {
        return itemMapper.mapToDTO(itemService.getItemsOnUrgency(urgency));
    }

}
