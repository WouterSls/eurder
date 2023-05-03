package com.switchfully.eurder.components.itemComponent;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
@Transactional
class ItemService implements IItemService{

    private final ItemMapper itemMapper;
    private final IItemRepository itemRepository;

    @Autowired
    public ItemService(ItemMapper itemMapper,IItemRepository itemRepository) {
        this.itemMapper = itemMapper;
        this.itemRepository = itemRepository;
    }

    @Override
    public Item createNewItem(CreateItemDTO createItemDTO){
        verifyCreateItem(createItemDTO);
        Item.Urgency urgency = calculateStockUrgency(createItemDTO.getAmount());
        Item itemToBeAdded = itemMapper.mapToDomain(createItemDTO, urgency);
        return itemRepository.save(itemToBeAdded);
    }

    @Override
    public Item updateItemById(UpdateItemDTO updateItemDTO, UUID id){

        verifyUpdateItem(updateItemDTO);
        verifyId(id);
        validateList();

        Item itemToUpdate = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalIdException("no item exists for provided it"));

        itemToUpdate.setAmount(updateItemDTO.getAmount());
        itemToUpdate.setName(updateItemDTO.getName());
        itemToUpdate.setPrice(updateItemDTO.getPrice());
        itemToUpdate.setDescription(updateItemDTO.getDescription());
        itemToUpdate.setUrgency(calculateStockUrgency(updateItemDTO.getAmount()));

        itemRepository.save(itemToUpdate);

        return itemToUpdate;
    }

    @Override
    public Item findById(UUID id){

        validateList();

        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new IllegalIdException("No item exists for this ID"));

        return item;
    }

    @Override
    public List<Item> getItemsSortedByUrgency(){

       validateList();

        return itemRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Item::getUrgency))
                .toList();

    }

    @Override
    public List<Item> getItemsOnUrgency(String urgency){


        validateList();

        return itemRepository.findAll().stream()
                .filter(item -> item.getUrgency().getLabel().equalsIgnoreCase(urgency))
                .toList();
    }

    @Override
    public List<Item> findAllItems(){
        return itemRepository.findAll();
    }

    private void verifyId(UUID id){
        if (id == null){
            throw new IllegalIdException("Please provide an ID");
        }

        boolean itemExist = itemRepository.findAll().stream().anyMatch(item -> item.getId().equals(id));
        if (!itemExist){
            throw new IllegalIdException("no items exists for provided id");
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