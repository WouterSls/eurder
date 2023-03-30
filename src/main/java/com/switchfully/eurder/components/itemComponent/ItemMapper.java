package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class ItemMapper{

    List<ItemDTO> mapToDTO(List<Item> items){
        return items.stream()
                .map(this::mapToDTO)
                .toList();
    }

    ItemDTO mapToDTO(Item item){
        return new ItemDTO(item.getId(),
                item.getName(),
                item.getDescription(),
                item.getPrice(),
                item.getAmount());
    }

    Item mapToDomain(CreateItemDTO createItemDTO){
        return new Item(createItemDTO.getName(),
                createItemDTO.getDescription(),
                createItemDTO.getPrice(),
                createItemDTO.getAmount());

    }
}