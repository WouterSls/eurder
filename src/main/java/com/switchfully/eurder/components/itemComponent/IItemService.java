package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;

import java.util.List;
import java.util.UUID;

public interface IItemService {
    ItemDTO createNewItem(CreateItemDTO createItemDTO);

    //List<ItemDTO> getListItemsDTO();

    ItemDTO getItemById(UUID uuid);

    ItemDTO updateItemById(UpdateItemDTO updateItemDTO, UUID id);

    List<ItemDTO> getItemsSortedByUrgency();

    List<ItemDTO> getItemsOnUrgency(String urgency);
}