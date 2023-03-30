package com.switchfully.eurder.components.itemComponent;

import java.util.UUID;

class Item{

    private final UUID id;
    private String name;
    private String description;
    private double price;
    private int amount;

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

    void setName(String name) {
        this.name = name;
    }

    void setDescription(String description) {
        this.description = description;
    }

    void setPrice(double price) {
        this.price = price;
    }

    void setAmount(int amount) {
        this.amount = amount;
    }
}