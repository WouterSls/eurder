package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.components.itemComponent.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemMapper{

    public List<ItemDTO> mapToDTO(List<Item> items){
        return items.stream()
                .map(this::mapToDTO)
                .toList();
    }
    public ItemDTO mapToDTO(Item item){
        return new ItemDTO(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount());
    }

    public Item mapToDomain(CreateItemDTO createItemDTO){
        return new Item(createItemDTO.getName(),
                createItemDTO.getDescription(),
                createItemDTO.getPrice(),
                createItemDTO.getAmount());

    }
}