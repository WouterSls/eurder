package com.switchfully.eurder.OrderComponent;

import com.switchfully.eurder.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

public class OrderServiceItemComponentIntegrationTest {
    //TODO: check if the ItemService works in OrderComponent

    private IItemService itemService;
    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;

    @BeforeEach
    void setup(){
        orderRepository = new OrderRepository();
        orderMapper = new OrderMapper();
        orderService = new OrderService(orderRepository,orderMapper,itemService);
        itemService = Mockito.mock(IItemService.class);
    }

    @Test
    @DisplayName("Integration test for itemComponent in orderService")
    void IntegrationTest (){
        private final ItemGroupDTO();
        private final CreateOrderDTO();
    }
}
