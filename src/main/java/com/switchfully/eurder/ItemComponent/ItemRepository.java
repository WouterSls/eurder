package com.switchfully.eurder.ItemComponent;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
class ItemRepository{
    List<Item> items;

    public ItemRepository() {
        this.items = new ArrayList<>();
    }

    void addItem(Item item){
        items.add(item);
    }

    public List<Item> getItems() {
        return items;
    }
}