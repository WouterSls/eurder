package com.switchfully.eurder.components.ItemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;

import java.util.List;

public interface IItemService {
    ItemDTO createNewItem(CreateItemDTO createItemDTO);

    List<ItemDTO> getListItemsDTO();

    ItemDTO getItemByName(String name);

    ItemDTO getItemById(String id);
}