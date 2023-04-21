package com.switchfully.eurder.api.dto.order;

import com.switchfully.eurder.api.dto.order.itemGroup.OrderItemGroupDTO;

import java.util.List;

public class CreateOrderDTO {

    List<OrderItemGroupDTO> orders;

    public CreateOrderDTO(){
    }

    public CreateOrderDTO(List<OrderItemGroupDTO> orders) {
        this.orders = orders;
    }

    public List<OrderItemGroupDTO> getOrders() {
        return orders;
    }
}
