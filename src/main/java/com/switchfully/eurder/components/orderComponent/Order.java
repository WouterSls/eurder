package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

class Order {

    private final UUID id;
    private final int amountOrdered;
    private final ItemDTO item;
    private final LocalDate shippingDate;
    private final double totalPrice;
    private final CustomerDTO customer;


    public Order(ItemDTO item, int amountOrdered, CustomerDTO customer) {
        this.id = UUID.randomUUID();
        this.item = item;
        this.amountOrdered = amountOrdered;
        this.shippingDate = calculateShippingDate();
        this.totalPrice = item.getPrice() * amountOrdered;
        this.customer = customer;
    }

    private LocalDate calculateShippingDate(){
        if (item.getAmount()>0){
            return LocalDate.now().plusDays(1);
        }
        else{
            return LocalDate.now().plusDays(7);
        }
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
