package com.switchfully.eurder.components.itemComponent;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
class ItemRepository{
    private List<Item> items;

    public ItemRepository() {
        this.items = new ArrayList<>();
    }

    void addItem(Item item){
        items.add(item);
    }

    void updateItem(Item item){
        items.set(items.indexOf(item),item);
    }

    List<Item> getItems() {
        return items;
    }

    Optional<Item> getItemById(UUID id){
        return items.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst();
    }
}