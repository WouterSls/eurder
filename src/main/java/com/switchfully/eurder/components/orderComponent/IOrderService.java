package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.order.CreateOrdersDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public interface IOrderService {

    List<OrderDTO> orderItems(CreateOrdersDTO createOrdersDTO, Jwt jwt);

    String reportOrdersByCustomer(Jwt jwt);

    OrderDTO reorderExistingOrder(UUID orderId, Jwt jwt);

    String getShippingList();
}
