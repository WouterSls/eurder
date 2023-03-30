package com.switchfully.eurder.components.OrderComponent;

import com.switchfully.eurder.components.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;

class OrderServiceTest {

    @Nested
    @DisplayName("unit test with mocking")
    class unitTestMocking{

        private OrderService orderService;
        private OrderRepository orderRepositoryMock;
        private OrderMapper orderMapperMock;
        private IItemService itemServiceMock;

        private final CreateOrderDTO TEST_CREATE_ORDER_DTO = new CreateOrderDTO(List.of(new ItemGroupDTO("foo",5)));

        @BeforeEach
        void setup(){
            orderRepositoryMock = Mockito.mock(OrderRepository.class);
            orderMapperMock = Mockito.mock(OrderMapper.class);
            itemServiceMock = Mockito.mock(IItemService.class);
            orderService = new OrderService(orderRepositoryMock,orderMapperMock,itemServiceMock);
        }

        @Test
        void orderItem_CreateOrderDTONotPresent_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
               orderService.orderItems(null);
            });
        }

        @Test
        void orderItem_CreateOrderDTOIdNotPresent_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new ItemGroupDTO(null,2))));
            });
        }
    }

}