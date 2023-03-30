package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.exception.*;
import com.switchfully.eurder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class ItemService implements IItemService{

    private final ItemRepository itemRepository;
    private final ItemMapper itemMapper;
    private final Utils utils;

    @Autowired
    public ItemService(ItemRepository itemRepository, ItemMapper itemMapper) {
        this.itemRepository = itemRepository;
        this.itemMapper = itemMapper;
        this.utils = new Utils();
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

    @Override
    public ItemDTO getItemByName(String name) {
        Optional<Item> gottenItem = itemRepository.getItemByName(name);
        if (gottenItem.isEmpty()){
            throw new IllegalIdException("Please provide a correct Item id (name)");
        }
        return itemMapper.mapToDTO(gottenItem.get());
    }

    @Override
    public ItemDTO getItemById(String id){
        boolean wildcard = id.contains("*");
        if (wildcard){
            String itemIdWithoutWildcard = id.replace("*","");
            Optional<Item> itemWithWildcard = itemRepository.getItems().stream()
                    .filter(item -> item.getId().toString().contains(itemIdWithoutWildcard))
                    .findFirst();
            if (itemWithWildcard.isEmpty()){
                throw new InvalidIdException("Please provide a correct Item ID");
            }
            return itemMapper.mapToDTO(itemWithWildcard.get());
        }

        if (!utils.isValidUUIDFormat(id)){
            throw new InvalidIdException("Please provide a valid ID format");
        }
        UUID itemID = UUID.fromString(id);
        Optional<Item> item = itemRepository.getItemById(itemID);
        if (item.isEmpty()){
            throw new IllegalIdException("Please provide a correct Item ID");
        }
        return itemMapper.mapToDTO(item.get());
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