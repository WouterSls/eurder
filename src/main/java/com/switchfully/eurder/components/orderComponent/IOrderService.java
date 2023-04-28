package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public interface IOrderService {

    Order orderItems(CreateOrderDTO createOrderDTO, Jwt jwt);

    String reportOrdersByCustomer(Jwt jwt);

    Order reorderExistingOrder(UUID orderId, Jwt jwt);

    String getShippingList();

    List<Order> findAllOrders();
}
