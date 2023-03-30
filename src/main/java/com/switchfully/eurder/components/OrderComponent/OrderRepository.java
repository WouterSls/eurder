package com.switchfully.eurder.components.OrderComponent;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

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
