package com.switchfully.eurder.api.dto.order.itemGroup;

import java.util.UUID;

public class OneOrderDTO {
    private final UUID id;
    private final int amountOrdered;

    public OneOrderDTO(UUID id, int amountOrdered) {
        this.id = id;
        this.amountOrdered = amountOrdered;
    }

    public UUID getId() {
        return id;
    }

    public int getAmountOrdered() {
        return amountOrdered;
    }
}
