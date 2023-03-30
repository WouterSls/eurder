package com.switchfully.eurder.components.itemComponent;

import java.util.UUID;

class Item{

    private final UUID id;
    private final String name;
    private final String description;
    private final double price;
    private final int amount;

    public Item(String name, String description, double price, int amount) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
    }

    String getName() {
        return name;
    }

    String getDescription() {
        return description;
    }

    double getPrice() {
        return price;
    }

    int getAmount() {
        return amount;
    }

    UUID getId(){
        return id;
    }
}