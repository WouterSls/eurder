package com.switchfully.eurder.components.itemComponent;

import java.util.UUID;

class Item{

    private final UUID id;
    private String name;
    private String description;
    private double price;
    private int amount;
    private Urgency urgency;

    public Item(String name, String description, double price, int amount) {
        this.id = UUID.randomUUID();
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.urgency = calculateStockUrgency(amount);
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

    public Urgency getUrgency() {
        return urgency;
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


    private Urgency calculateStockUrgency(int stockAmount){
        if (stockAmount < 5){
            return Urgency.STOCK_LOW;
        }
        if (stockAmount <10){
            return Urgency.STOCK_MEDIUM;
        }
        return Urgency.STOCK_HIGH;
    }
    @Override
    public String toString() {
        return "Item: " + getName() + "\nDescription: " + getDescription() + "\nPrice: " + getPrice() + "\nAmount in stock: " + getAmount() + "\n\nItem ID: " + getId();
    }
}