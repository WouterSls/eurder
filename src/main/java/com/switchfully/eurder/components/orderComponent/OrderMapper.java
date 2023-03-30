package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.order.OrderDTO;
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
        return new OrderDTO(order.getId(),order.getAmountOrdered(),order.getItem(),order.getShippingDate(),order.getTotalPrice());
    }

    Order mapToDomain(OrderDTO orderDTO){
        return new Order(orderDTO.getItem(),orderDTO.getAmountOrdered());
    }

    List<Order> mapToDomain(List<OrderDTO> orderDTOList){
        return orderDTOList.stream()
                .map(this::mapToDomain)
                .toList();
    }
}


