package com.switchfully.eurder.OrderComponent;

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

    //TODO: Add a way to keep track of the customer

    /*
        This is most likely overhead, pass the customer via header in orderService and save it.
     */

    public Order(ItemDTO item, int amountOrdered) {
        this.id = UUID.randomUUID();
        this.item = item;
        this.amountOrdered = amountOrdered;
        this.shippingDate = calculateShippingDate();
        this.totalPrice = item.getPrice() * amountOrdered;
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
