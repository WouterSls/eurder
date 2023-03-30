package com.switchfully.eurder.components.OrderComponent;

import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;

import java.util.List;

public interface IOrderService {

    List<OrderDTO> orderItems(CreateOrderDTO createOrderDTO);
}
