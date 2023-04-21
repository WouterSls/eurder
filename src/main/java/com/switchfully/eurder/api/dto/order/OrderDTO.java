package com.switchfully.eurder.api.dto.order;


import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.components.customerComponent.Customer;
import com.switchfully.eurder.components.itemComponent.Item;

import java.time.LocalDate;
import java.util.UUID;

public class OrderDTO {

    private final UUID id;
    private final int amountOrdered;
    private final Item item;
    private final LocalDate shippingDate;
    private final double totalPrice;
    private final Customer customer;

    public OrderDTO(UUID id, int amountOrdered, Item item, LocalDate shippingDate, double totalPrice, Customer customer) {
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

    public Item getItem() {
        return item;
    }

    public LocalDate getShippingDate() {
        return shippingDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public Customer getCustomer() {
        return customer;
    }
}
