package com.switchfully.eurder.api.dto.order;


import com.switchfully.eurder.api.dto.item.ItemDTO;

import java.time.LocalDate;
import java.util.UUID;

public class OrderDTO {

    private final UUID id;
    private final int amountOrdered;
    private final ItemDTO item;
    private final LocalDate shippingDate;
    private final double totalPrice;

    public OrderDTO(UUID id, int amountOrdered, ItemDTO item, LocalDate shippingDate, double totalPrice) {
        this.id = id;
        this.amountOrdered = amountOrdered;
        this.item = item;
        this.shippingDate = shippingDate;
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public int getAmountOrdered() {
        return amountOrdered;
    }

    public ItemDTO getItem() {
        return item;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }
}
