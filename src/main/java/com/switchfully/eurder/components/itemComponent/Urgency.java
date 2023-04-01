package com.switchfully.eurder.components.itemComponent;


enum Urgency {
    STOCK_LOW("high"),STOCK_MEDIUM("medium"), STOCK_HIGH("low");

    private final String label;

    Urgency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
