package com.switchfully.eurder.components.orderComponent;

import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
class OrderRepository {

    List<Order> orders;


    OrderRepository() {
        this.orders = new ArrayList<>();
    }

    List<Order> getOrders() {
        return orders;
    }

    void addOrders(List<Order> orders){
        this.orders.addAll(orders);
    }

}
