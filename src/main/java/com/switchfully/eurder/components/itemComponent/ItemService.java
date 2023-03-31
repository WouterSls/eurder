package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
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
        verifyCreateItem(createItemDTO);
        Item itemToBeAdded = itemMapper.mapToDomain(createItemDTO);
        itemRepository.addItem(itemToBeAdded);
        return itemMapper.mapToDTO(itemToBeAdded);
    }

    @Override
    public List<ItemDTO> getListItemsDTO() {
        if (itemRepository.getItems().isEmpty()){
            throw new NoItemsException("No items present");
        }
        return itemMapper.mapToDTO(itemRepository.getItems());
    }

    @Override
    public ItemDTO updateItemById(UpdateItemDTO updateItemDTO, String id){
        verifyUpdateItem(updateItemDTO);
        verifyId(id);
        Optional<Item> itemToUpdate = itemRepository.getItemById(UUID.fromString(id));

        if (itemToUpdate.isEmpty()){
            throw new IllegalIdException("Please provide a correct id");
        }

        itemToUpdate.get().setAmount(updateItemDTO.getAmount());
        itemToUpdate.get().setName(updateItemDTO.getName());
        itemToUpdate.get().setPrice(updateItemDTO.getPrice());
        itemToUpdate.get().setDescription(updateItemDTO.getDescription());

        itemRepository.updateItem(itemToUpdate.get());

        return itemMapper.mapToDTO(itemToUpdate.get());
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
                throw new InvalidIdFormatException("No item exists for this ID");
            }
            return itemMapper.mapToDTO(itemWithWildcard.get());
        }

        if (!utils.isValidUUIDFormat(id)){
            throw new InvalidIdFormatException("Please provide a valid ID format");
        }
        UUID itemID = UUID.fromString(id);
        Optional<Item> item = itemRepository.getItemById(itemID);
        if (item.isEmpty()){
            throw new IllegalIdException("No item exists for this ID");
        }
        return itemMapper.mapToDTO(item.get());
    }


    private void verifyId(String id){
        ItemDTO itemById = getItemById(id);
        if (itemById == null){
            throw new IllegalIdException("verifyId: no ItemDTO found");
        }
        boolean idExists = itemRepository.getItems().stream().anyMatch(item -> item.getId().equals(itemById.getId()));
        if (!idExists){
            throw new IllegalIdException("Please provide a correct item Id");
        }
    }
    private void verifyUpdateItem(UpdateItemDTO updateItemDTO){
        if (updateItemDTO == null){
            throw new MandatoryFieldException("Please provide an updated item");
        }
        if (updateItemDTO.getName() == null || updateItemDTO.getName().isEmpty() || updateItemDTO.getName().equals("")){
            throw new MandatoryFieldException("Please provide a name");
        }
        if (updateItemDTO.getDescription() == null || updateItemDTO.getDescription().isEmpty() ||updateItemDTO.getDescription().equals("")){
            throw new MandatoryFieldException("Please provide a description");
        }
        if (updateItemDTO.getPrice() <= 0){
            throw new IllegalPriceException("Please provide a price higher than 0");
        }
        if (updateItemDTO.getAmount() <= 0){
            throw new IllegalAmountException("Please provide an amount higher than 0");
        }
    }

    private void verifyCreateItem(CreateItemDTO createItemDTO){
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