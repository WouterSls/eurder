package com.switchfully.eurder.api.dto.order;

import java.util.UUID;

public class OrderItemGroupDTO {
    private final UUID id;
    private final int amountOrdered;

    public OrderItemGroupDTO(UUID id, int amountOrdered) {
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
