package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;
import java.util.UUID;

public interface IOrderService {

    List<OrderDTO> orderItems(CreateOrderDTO createOrderDTO, Jwt jwt);

    String reportOrdersByCustomer(Jwt jwt);

    OrderDTO reorderExistingOrder(UUID orderId, Jwt jwt);

    String getShippingList();
}
