package com.switchfully.eurder.api.dto.order;

import com.switchfully.eurder.components.customerComponent.Customer;
import com.switchfully.eurder.components.orderComponent.ItemGroup;

import java.util.List;
import java.util.UUID;

public class OrderDTO{

    private final UUID id;
    private final List<ItemGroup> itemsGroups;
    private final Customer customer;
    private final double totalPrice;

    public OrderDTO(UUID id, List<ItemGroup> itemGroups, Customer customer, double totalPrice) {
        this.id = id;
        this.itemsGroups = itemGroups;
        this.customer = customer;
        this.totalPrice = totalPrice;
    }

    public UUID getId() {
        return id;
    }

    public List<ItemGroup> getItemGroups() {
        return itemsGroups;
    }

    public Customer getCustomer() {
        return customer;
    }

    public double getTotalPrice(){
        return totalPrice;
    }
}
