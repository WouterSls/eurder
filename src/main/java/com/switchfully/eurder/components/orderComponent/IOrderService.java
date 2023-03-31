package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;

import java.util.List;
import java.util.UUID;

public interface IOrderService {

    List<OrderDTO> orderItems(CreateOrderDTO createOrderDTO, String auth);

    String reportOrdersByCustomer(String auth);

    OrderDTO reorderExistingOrder(UUID orderId, String auth);

    OrderDTO getOrderById(UUID id);
}
