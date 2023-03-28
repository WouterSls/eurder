package com.switchfully.eurder.ItemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;

import java.util.List;

public interface IItemService {
    ItemDTO createNewItem(CreateItemDTO createItemDTO);

    List<ItemDTO> getListItemsDTO();
}