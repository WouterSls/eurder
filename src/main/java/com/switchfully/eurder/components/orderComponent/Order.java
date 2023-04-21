package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.components.customerComponent.Customer;
import com.switchfully.eurder.components.itemComponent.Item;
import jakarta.persistence.*;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "orders")
class Order {

    @Id
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "fk_customer_id")
    private Customer customer;

    @Column(name = "amount_ordered")
    private int amountOrdered;

    @ManyToOne
    @JoinColumn(name = "fk_item_id")
    private Item item;

    @Column(name = "total_price")
    private double totalPrice;

    @Column(name = "shipping_date", columnDefinition = "date")
    private LocalDate shippingDate;



    public Order(Item item, int amountOrdered, Customer customer) {
        this.id = UUID.randomUUID();
        this.item = item;
        this.amountOrdered = amountOrdered;
        this.shippingDate = calculateShippingDate();
        this.totalPrice = item.getPrice() * amountOrdered;
        this.customer = customer;
    }

    public Order(){

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
