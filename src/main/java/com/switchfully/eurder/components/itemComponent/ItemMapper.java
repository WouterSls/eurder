package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.components.itemComponent.Item;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

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

    public Item mapToDomain(CreateItemDTO createItemDTO, Item.Urgency urgency){
        return new Item(UUID.randomUUID(),
                createItemDTO.getName(),
                createItemDTO.getDescription(),
                createItemDTO.getPrice(),
                createItemDTO.getAmount(),
                urgency);

    }
    private Item.Urgency calculateStockUrgency(int stockAmount){
        if (stockAmount < 5){
            return Item.Urgency.STOCK_LOW;
        }
        if (stockAmount <10){
            return Item.Urgency.STOCK_MEDIUM;
        }
        return Item.Urgency.STOCK_HIGH;
    }
}