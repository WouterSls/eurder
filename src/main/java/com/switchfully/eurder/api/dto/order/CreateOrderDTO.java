package com.switchfully.eurder.api.dto.order;

import com.switchfully.eurder.api.dto.order.itemGroup.CreateItemGroupDTO;

import java.util.List;

public class CreateOrderDTO {

    List<CreateItemGroupDTO> itemsToOrder;

    public CreateOrderDTO(){
    }

    public CreateOrderDTO(List<CreateItemGroupDTO> itemsToOrder) {
        this.itemsToOrder = itemsToOrder;
    }

    public List<CreateItemGroupDTO> getItemsToOrder() {
        return itemsToOrder;
    }
}
