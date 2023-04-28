package com.switchfully.eurder.api.dto.order.itemGroup;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.util.UUID;

public class CreateItemGroupDTO {
    private final UUID id;
    private final int amountOrdered;
    @JsonIgnore
    private final LocalDate shippingDate;

    public CreateItemGroupDTO(UUID id, int amountOrdered, LocalDate shippingDate) {
        this.id = id;
        this.amountOrdered = amountOrdered;
        this.shippingDate = shippingDate;
    }

    public UUID getId() {
        return id;
    }

    public int getAmountOrdered() {
        return amountOrdered;
    }

    public LocalDate getShippingDate(){
        return shippingDate;
    }
}
