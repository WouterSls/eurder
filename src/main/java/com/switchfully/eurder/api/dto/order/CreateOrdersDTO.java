package com.switchfully.eurder.api.dto.order;

import com.switchfully.eurder.api.dto.order.itemGroup.OneOrderDTO;

import java.util.List;

public class CreateOrdersDTO {

    List<OneOrderDTO> orders;

    public CreateOrdersDTO(){
    }

    public CreateOrdersDTO(List<OneOrderDTO> orders) {
        this.orders = orders;
    }

    public List<OneOrderDTO> getOrders() {
        return orders;
    }
}
