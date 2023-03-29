package com.switchfully.eurder.OrderComponent;

import com.switchfully.eurder.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.zExceptions.IllegalAmountException;
import com.switchfully.eurder.zExceptions.MandatoryFieldException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

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

        @Test
        void orderItem_CreateOrderDTOAmountEquals0_returnsIllegalAmountException(){
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new ItemGroupDTO("foo",0))));
            });
        }

        @Test
        void orderItem_CreateOrderDTOAmountUnder0_returnsIllegalAmountException(){
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new ItemGroupDTO("foo",-5))));
            });
        }

        @Test
        void orderItem_CreateOrderDTOPresent_callCorrectFunction(){
            orderService.orderItems(TEST_CREATE_ORDER_DTO);

            Mockito.verify(orderRepositoryMock,Mockito.never()).getOrders();
        }

        @Test
        void orderItem_CreateOrderDTOPresent_returnsOrderDTOList(){
            final ItemDTO TEST_ITEM = new ItemDTO("foo","bar",20,3);
            final Order TEST_ORDER = new Order(TEST_ITEM,1);
            Mockito.when(orderService.orderItems(TEST_CREATE_ORDER_DTO))
                    .thenReturn(orderMapperMock.mapToDTO(List.of(TEST_ORDER)));

            List<OrderDTO> actualOrderDTOList = orderService.orderItems(TEST_CREATE_ORDER_DTO);

            Assertions.assertEquals(orderService.orderItems(TEST_CREATE_ORDER_DTO),actualOrderDTOList);
        }

        //TODO: add more orderService testing
    }

}