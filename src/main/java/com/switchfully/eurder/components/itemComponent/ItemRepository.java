package com.switchfully.eurder.components.itemComponent;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class ItemRepository{
    List<Item> items;

    private final Item TEST_ITEM = new Item("foo","bar",10,5);

    public ItemRepository() {
        this.items = new ArrayList<>();
        items.add(TEST_ITEM);
    }

    void addItem(Item item){
        items.add(item);
    }

    List<Item> getItems() {
        return items;
    }

    Optional<Item> getItemByName(String name){
        return items.stream()
                .filter(item -> item.getName().equals(name))
                .findFirst();
    }

    Optional<Item> getItemById(UUID id){
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }
}