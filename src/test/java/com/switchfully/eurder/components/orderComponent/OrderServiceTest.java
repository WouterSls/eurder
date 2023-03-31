package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.components.securityComponent.ISecurityService;
import com.switchfully.eurder.exception.IllegalAmountException;
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
        private ISecurityService securityService;
        private ICustomerService customerService;

        private final CreateOrderDTO TEST_CREATE_ORDER_DTO = new CreateOrderDTO(List.of(new ItemGroupDTO("foo", 5)));

        CustomerDTO customerDTO = new CustomerDTO("foo","bar","test","foo","bar", UUID.randomUUID());
        String encodedAuth;

        @BeforeEach
        void setup() {
            orderRepositoryMock = Mockito.mock(OrderRepository.class);
            orderMapperMock = Mockito.mock(OrderMapper.class);
            itemServiceMock = Mockito.mock(IItemService.class);
            securityService = Mockito.mock(ISecurityService.class);
            customerService = Mockito.mock(ICustomerService.class);

            String userId = customerDTO.getId().toString();
            encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userId + ":password").getBytes());

            orderService = new OrderService(orderRepositoryMock, orderMapperMock, itemServiceMock,securityService,customerService);
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
                orderService.orderItems(new CreateOrderDTO(List.of(new ItemGroupDTO(null, 2))),null);
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

        @Test
        void orderItems_ItemGroupDTOPresentNoCustomerIdPresent_returnsMadatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
               orderService.orderItems(TEST_CREATE_ORDER_DTO,encodedAuth);
            });
        }

        @Test
        void orderItems_ItemGroupDTOPresentInvalidUUIDFormat_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
               orderService.orderItems(TEST_CREATE_ORDER_DTO, "12345");
            });
        }

        @Test
        void reportOrdersByCustomer_InvalidAuthString_returnsInvalidIdFormatException(){
            Assertions.assertThrows(InvalidIdFormatException.class, () -> {
               orderService.reportOrdersByCustomer("Basic 12345:password");
            });
        }

        @Test
        void reportOrdersByCustomer_AuthStringNotPresent_returnsMandatoryFieldException(){
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
               orderService.reportOrdersByCustomer(null);
            });
        }
    }

}