package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.List;

import static java.util.Collections.emptyList;

class OrderServiceTest {

    @Nested
    @DisplayName("unit test with mocking")
    class unitTestMocking {

        private OrderService orderService;
        private OrderRepository orderRepositoryMock;
        private OrderMapper orderMapperMock;
        private IItemService itemServiceMock;

        private final CreateOrderDTO TEST_CREATE_ORDER_DTO = new CreateOrderDTO(List.of(new ItemGroupDTO("foo", 5)));

        @BeforeEach
        void setup() {
            orderRepositoryMock = Mockito.mock(OrderRepository.class);
            orderMapperMock = Mockito.mock(OrderMapper.class);
            itemServiceMock = Mockito.mock(IItemService.class);
            orderService = new OrderService(orderRepositoryMock, orderMapperMock, itemServiceMock);
        }

        @Test
        void orderItem_CreateOrderDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(null);
            });
        }

        @Test
        void orderItem_CreateOrderDTOIdNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new ItemGroupDTO(null, 2))));
            });
        }

        @Test
        void verifyOrder_ItemGroupDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.verifyOrder(new CreateOrderDTO(null));
            });
        }

        @Test
        void verifyOrder_CreateOrderDTOEmptyList_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.verifyOrder(new CreateOrderDTO(emptyList()));
            });
        }

        @Test
        void verifyOrder_ItemGroupDTOIdNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.verifyOrder(new CreateOrderDTO(List.of(new ItemGroupDTO(null, 2))));
            });
        }

        @Test
        void verifyOrder_ItemGroupDTOAmountUnder0_returnsIllegalAmountException() {
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                orderService.verifyOrder(new CreateOrderDTO(List.of(new ItemGroupDTO("foo", -5))));
            });
        }
    }

}