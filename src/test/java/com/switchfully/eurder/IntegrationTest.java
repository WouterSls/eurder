package com.switchfully.eurder.OrderComponent;

import com.switchfully.eurder.components.CustomerComponent.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class IntegrationTest {
    //TODO: check if the ItemService works in OrderComponent

    @Autowired
    private IItemService itemService;
    private OrderService orderService;
    private OrderRepository orderRepository;
    private OrderMapper orderMapper;
    final CreateItemDTO createItemDTO = new CreateItemDTO("foo","bar",10,5);


    @BeforeEach
    void setup(){
        orderRepository = new OrderRepository();
        orderMapper = new OrderMapper();
        orderService = new OrderService(orderRepository,orderMapper,itemService);
    }

    @Test
    @DisplayName("Integration test for itemComponent in orderService")
    void IntegrationTest (){
        final ItemGroupDTO itemGroupDTO = new ItemGroupDTO("foo",1);
        final CreateOrderDTO CreateOrderDTO = new CreateOrderDTO(List.of(itemGroupDTO));


        itemService.createNewItem(createItemDTO);
        ItemDTO createdItem = itemService.getItemByName("foo");
        Assertions.assertNotNull(createdItem);
    }
}
