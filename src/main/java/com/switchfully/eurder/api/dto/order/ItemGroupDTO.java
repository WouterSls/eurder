package com.switchfully.eurder.api.dto.order;

public class ItemGroupDTO {
    private final String id;
    private final int amountOrdered;

    public ItemGroupDTO(String id, int amountOrdered) {
        this.id = id;
        this.amountOrdered = amountOrdered;
    }

    public String getId() {
        return id;
    }

    public int getAmountOrdered() {
        return amountOrdered;
    }
}
