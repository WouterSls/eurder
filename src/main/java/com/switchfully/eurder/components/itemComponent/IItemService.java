package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;

import java.util.List;

public interface IItemService {
    ItemDTO createNewItem(CreateItemDTO createItemDTO);

    List<ItemDTO> getListItemsDTO();

    ItemDTO getItemById(String id);

    ItemDTO updateItemById(UpdateItemDTO updateItemDTO, String id);

    List<ItemDTO> getItemsStock();

    List<ItemDTO> getItemsStockByUrgency(String urgency);
}