package com.switchfully.eurder.api.dto.order;

import java.util.List;

public class CreateOrderDTO {

    List<ItemGroupDTO> orders;

    public CreateOrderDTO(){
    }

    public CreateOrderDTO(List<ItemGroupDTO> orders) {
        this.orders = orders;
    }

    public List<ItemGroupDTO> getOrders() {
        return orders;
    }
}
