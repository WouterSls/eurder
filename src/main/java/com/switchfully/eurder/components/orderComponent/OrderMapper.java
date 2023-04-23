package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.components.customerComponent.Customer;
import com.switchfully.eurder.components.itemComponent.Item;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class OrderMapper {

    List<OrderDTO> mapToDTO(List<Order> orders){
        return orders.stream()
                .map(this::mapToDTO)
                .toList();
    }

    OrderDTO mapToDTO(Order order){
        return new OrderDTO(order.getId(),order.getAmountOrdered(),order.getItem(),order.getShippingDate(),order.getTotalPrice(),order.getCustomer());
    }

    Order mapToDomain(Item item,int amountOrdered, Customer customer){
        return new Order(item,
                amountOrdered,
                customer);
    }
}


