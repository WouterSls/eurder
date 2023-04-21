package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
class ItemService implements IItemService{

    private final ItemMapper itemMapper;
    private final IItemRepository itemRepository;

    @Autowired
    public ItemService(ItemMapper itemMapper,IItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public ItemDTO createNewItem(CreateItemDTO createItemDTO){
        verifyCreateItem(createItemDTO);
        Item itemToBeAdded = itemMapper.mapToDomain(createItemDTO);
        itemRepository.save(itemToBeAdded);
        return itemMapper.mapToDTO(itemToBeAdded);
    }

    @Override
    public ItemDTO updateItemById(UpdateItemDTO updateItemDTO, UUID id){

        verifyUpdateItem(updateItemDTO);
        verifyId(id);
        validateList();

        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalIdException("provide a correct id"));

        itemToUpdate.setAmount(updateItemDTO.getAmount());
        itemToUpdate.setName(updateItemDTO.getName());
        itemToUpdate.setPrice(updateItemDTO.getPrice());
        itemToUpdate.setDescription(updateItemDTO.getDescription());

        itemRepository.save(itemToUpdate);

        return itemMapper.mapToDTO(itemToUpdate);
    }

    @Override
    public ItemDTO getItemById(UUID id){

        validateList();

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalIdException("No item exists for this ID"));

        return itemMapper.mapToDTO(item);
    }

    @Override
    public List<ItemDTO> getItemsSortedByUrgency(){

       validateList();

        return itemRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Item::getUrgency))
                .map(itemMapper::mapToDTO)
                .toList();

    }

    @Override
    public List<ItemDTO> getItemsOnUrgency(String urgency){


        validateList();

        return itemRepository.findAll().stream()
                .filter(item -> item.getUrgency().getLabel().equalsIgnoreCase(urgency))
                .map(itemMapper::mapToDTO)
                .toList();
    }


    private void verifyId(UUID id){
        if (id == null){
            throw new IllegalIdException("Please provide an ID");
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
    private void validateList(){

        if (itemRepository.findAll().isEmpty()){
            throw new NoItemsException("There are currently no items in stock");
        }

    }

}