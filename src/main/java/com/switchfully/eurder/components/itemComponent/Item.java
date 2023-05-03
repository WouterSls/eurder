package com.switchfully.eurder.components.itemComponent;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "items")
public class Item{

    @Id
    private UUID id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "price")
    private double price;
    @Column(name="amount")
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "urgency")
    private Urgency urgency;

    public Item(UUID id,String name, String description, double price, int amount, Urgency urgency) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.amount = amount;
        this.urgency = urgency;
    }

    private Item(){

    }
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

    public UUID getId(){
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

    void setUrgency(Urgency urgency){
        this.urgency = urgency;
    }
    @Override
    public String toString() {
        return "Item: " + getName() + "\nDescription: " + getDescription() + "\nPrice: " + getPrice() + "\nAmount in stock: " + getAmount() + "\n\nItem ID: " + getId();
    }

    public enum Urgency {
        STOCK_LOW("high"),STOCK_MEDIUM("medium"), STOCK_HIGH("low");

        private final String label;

        Urgency(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }


    }
}