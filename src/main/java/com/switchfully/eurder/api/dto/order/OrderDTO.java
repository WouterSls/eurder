package com.switchfully.eurder.api.dto.order;


import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;

import java.time.LocalDate;
import java.util.UUID;

public class OrderDTO {

    private final UUID id;
    private final int amountOrdered;
    private final ItemDTO item;
    private final LocalDate shippingDate;
    private final double totalPrice;
    private final CustomerDTO customer;

    public OrderDTO(UUID id, int amountOrdered, ItemDTO item, LocalDate shippingDate, double totalPrice, CustomerDTO customer) {
        this.id = id;
        this.amountOrdered = amountOrdered;
        this.item = item;
        this.shippingDate = shippingDate;
        this.totalPrice = totalPrice;
        this.customer = customer;
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

    public double getTotalPrice() {
        return totalPrice;
    }

    public CustomerDTO getCustomer() {
        return customer;
    }
}
