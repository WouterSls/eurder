package com.switchfully.eurder.components.itemComponent;

enum Urgency {
    STOCK_LOW("low"),STOCK_MEDIUM("medium"), STOCK_HIGH("high");

    private final String label;

    Urgency(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
