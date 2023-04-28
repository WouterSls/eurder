package com.switchfully.eurder.components.orderComponent;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.switchfully.eurder.components.customerComponent.Customer;
import jakarta.persistence.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "purchase_orders" )
public class Order {

    @Id
    private UUID id;

    @OneToOne
    @JoinColumn(name = "fk_customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "fk_order", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ItemGroup> itemGroups;
    @Column(name = "total_price")
    private double totalPrice;

    public Order(UUID id, Customer customer, List<ItemGroup> itemGroups, double totalPrice){
        this.id = id;
        this.customer = customer;
        this.itemGroups = itemGroups;
        this.totalPrice = totalPrice;
    }

    public Order(){

    }

    public Customer getCustomer() {
        return customer;
    }

    public UUID getId() {
        return id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public List<ItemGroup> getItemGroups() {
        return itemGroups;
    }
}
