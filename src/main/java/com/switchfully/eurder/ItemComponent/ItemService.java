package com.switchfully.eurder.ItemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.zExceptions.IllegalAmountException;
import com.switchfully.eurder.zExceptions.IllegalPriceException;
import com.switchfully.eurder.zExceptions.MandatoryFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class ItemService implements IItemService{

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
    }

    @Override
    public ItemDTO createNewItem(CreateItemDTO createItemDTO){
        verifyItem(createItemDTO);
        Item itemToBeAdded = itemMapper.mapToDomain(createItemDTO);
        itemRepository.addItem(itemToBeAdded);
        return itemMapper.mapToDTO(itemToBeAdded);
    }

    @Override
    public List<ItemDTO> getListItemsDTO() {
        return itemMapper.mapToDTO(itemRepository.getItems());
    }

    private void verifyItem(CreateItemDTO createItemDTO){
        if (createItemDTO == null) {
            throw new MandatoryFieldException("Please provide an item to be added");
        }
        if (createItemDTO.getName() == null) {
            throw new MandatoryFieldException("The name of the item is a required field");
        }
        if (createItemDTO.getDescription() == null){
            throw new MandatoryFieldException("The description of the item is a required field");
        }
        if (createItemDTO.getPrice() == 0 || createItemDTO.getPrice() < 0){
            throw new IllegalPriceException("The price of the item should be higher than 0");
        }
        if (createItemDTO.getAmount() == 0 || createItemDTO.getAmount() < 0){
            throw new IllegalAmountException("Amount of item should be higher than 0");
        }
    }
}