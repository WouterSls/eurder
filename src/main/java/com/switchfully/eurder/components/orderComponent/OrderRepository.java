package com.switchfully.eurder.components.orderComponent;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class OrderRepository {

    private List<Order> orders;


    OrderRepository() {
        this.orders = new ArrayList<>();
    }

    List<Order> getOrders() {
        return orders;
    }

    void addOrder(List<Order> orders){
        this.orders.addAll(orders);
    }

    void addOrder(Order order){
        this.orders.add(order);
    }

}
