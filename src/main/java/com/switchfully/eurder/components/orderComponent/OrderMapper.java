package com.switchfully.eurder.components.orderComponent;


import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.components.customerComponent.Customer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
public class OrderMapper {

    public List<OrderDTO> mapToDTO(List<Order> orderSingles){
        return orderSingles.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public OrderDTO mapToDTO(Order order){
        return new OrderDTO(order.getId(),order.getItemGroups(),order.getCustomer(), order.getTotalPrice());
    }

    Order mapToDomain(List<ItemGroup> orderedItems, Customer customer, double totalPrice){

        return new Order(UUID.randomUUID(),
                customer,
                orderedItems,
                totalPrice);
    }
}


