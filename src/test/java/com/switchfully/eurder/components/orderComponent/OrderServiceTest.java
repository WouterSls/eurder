package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.OrderItemGroupDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.InvalidIdFormatException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

class OrderServiceTest {

    @Nested
    @DisplayName("unit test with mocking")
    class unitTestMocking {

        private OrderService orderService;
        private OrderRepository orderRepositoryMock;
        private OrderMapper orderMapperMock;
        private IItemService itemServiceMock;
        private ICustomerService customerService;

        private final CreateOrderDTO TEST_CREATE_ORDER_DTO = new CreateOrderDTO(List.of(new OrderItemGroupDTO(UUID.randomUUID(), 5)));

        CustomerDTO customerDTO = new CustomerDTO("foo","bar","test","foo","bar", UUID.randomUUID());
        String encodedAuth;

        @BeforeEach
        void setup() {
            orderRepositoryMock = Mockito.mock(OrderRepository.class);
            orderMapperMock = Mockito.mock(OrderMapper.class);
            itemServiceMock = Mockito.mock(IItemService.class);
            customerService = Mockito.mock(ICustomerService.class);

            String userId = customerDTO.getId().toString();
            encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userId + ":password").getBytes());

            orderService = new OrderService(orderRepositoryMock, orderMapperMock, itemServiceMock,customerService);
        }

        @Test
        void orderItem_CreateOrderDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(null,null);
            });
        }

        @Test
        void orderItem_CreateOrderDTOItemIdNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new OrderItemGroupDTO(null, 2))),null);
            });
        }

        @Test
        void verifyOrder_ItemGroupDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(null),encodedAuth);
            });
        }

        @Test
        void verifyOrder_CreateOrderDTOEmptyList_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(emptyList()),encodedAuth);
            });
        }

        @Test
        void verifyOrder_ItemGroupDTOIdNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new OrderItemGroupDTO(null, 2))),encodedAuth);
            });
        }

        @Test
        void verifyOrder_ItemGroupDTOAmountUnder0_returnsIllegalAmountException() {
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                orderService.orderItems(new CreateOrderDTO(List.of(new OrderItemGroupDTO(UUID.randomUUID(), -5))),encodedAuth);
            });
        }

        @Test
        void orderItems_ItemGroupDTOPresentNoItemForId_returnsIllegalFieldException(){
            Assertions.assertThrows(IllegalIdException.class, () -> {
               orderService.orderItems(TEST_CREATE_ORDER_DTO,encodedAuth);
            });
        }

        @Test
        void reorderExistingOrder_AuthStringPresentWrongOrderID_returnsIllegalIdException(){

            Assertions.assertThrows(IllegalIdException.class, () -> {
               orderService.reorderExistingOrder(UUID.randomUUID(),"Basic username:password");
            });
        }

        @Test
        void reorderExistingOrder_AuthStringPresentNoOrderID_returnsIllegalIdException(){
            Assertions.assertThrows(IllegalIdException.class,() -> {
               orderService.reorderExistingOrder(null,"Basic username:password");
            });
        }
    }

}