package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;

import java.util.List;
import java.util.UUID;

public interface IItemService {
    Item createNewItem(CreateItemDTO createItemDTO);

    Item findById(UUID uuid);

    Item updateItemById(UpdateItemDTO updateItemDTO, UUID id);

    List<Item> getItemsSortedByUrgency();

    List<Item> getItemsOnUrgency(String urgency);

    List<Item> findAllItems();
}