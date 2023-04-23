package com.switchfully.eurder.components.orderComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.order.CreateOrdersDTO;
import com.switchfully.eurder.api.dto.order.itemGroup.OneOrderDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerRepository;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemRepository;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.exception.IllegalAmountException;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static java.util.Collections.emptyList;

class OrderServiceTest {

    @Nested
    @DisplayName("unit test with mocking")
    class unitTestMocking {

        private OrderService orderService;
        private IOrderRepository orderRepositoryMock;
        private OrderMapper orderMapperMock;
        private IItemService itemServiceMock;
        private IItemRepository itemRepositoryMock;
        private ICustomerService customerService;
        private ICustomerRepository customerRepoMock;

        private final CreateOrdersDTO TEST_CREATE_ORDER_DTO = new CreateOrdersDTO(List.of(new OneOrderDTO(UUID.randomUUID(), 5)));

        Jwt jwt = new Jwt("test", Instant.now(),Instant.now().plusSeconds(300),null,null);

        CustomerDTO customerDTO = new CustomerDTO("foo","bar","test","foo","bar", UUID.randomUUID());
        String encodedAuth;

        @BeforeEach
        void setup() {
            orderRepositoryMock = Mockito.mock(IOrderRepository.class);
            orderMapperMock = Mockito.mock(OrderMapper.class);
            itemServiceMock = Mockito.mock(IItemService.class);
            customerService = Mockito.mock(ICustomerService.class);
            itemRepositoryMock = Mockito.mock(IItemRepository.class);
            customerRepoMock = Mockito.mock(ICustomerRepository.class);

            String userId = customerDTO.getId().toString();
            encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userId + ":password").getBytes());

            orderService = new OrderService(orderRepositoryMock, orderMapperMock, itemServiceMock,customerService,customerRepoMock,itemRepositoryMock);
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
                orderService.orderItems(new CreateOrdersDTO(List.of(new OneOrderDTO(null, 2))),null);
            });
        }

        @Test
        void verifyOrder_ItemGroupDTONotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrdersDTO(null),jwt);
            });
        }

        @Test
        void verifyOrder_CreateOrderDTOEmptyList_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrdersDTO(emptyList()),jwt);
            });
        }

        @Test
        void verifyOrder_ItemGroupDTOIdNotPresent_returnsMandatoryFieldException() {
            Assertions.assertThrows(MandatoryFieldException.class, () -> {
                orderService.orderItems(new CreateOrdersDTO(List.of(new OneOrderDTO(null, 2))),jwt);
            });
        }

        @Test
        void verifyOrder_ItemGroupDTOAmountUnder0_returnsIllegalAmountException() {
            Assertions.assertThrows(IllegalAmountException.class, () -> {
                orderService.orderItems(new CreateOrdersDTO(List.of(new OneOrderDTO(UUID.randomUUID(), -5))),jwt);
            });
        }

        @Test
        void orderItems_ItemGroupDTOPresentNoItemForId_returnsIllegalFieldException(){
            Assertions.assertThrows(IllegalIdException.class, () -> {
               orderService.orderItems(TEST_CREATE_ORDER_DTO,jwt);
            });
        }

        @Test
        void reorderExistingOrder_AuthStringPresentWrongOrderID_returnsIllegalIdException(){

            Assertions.assertThrows(IllegalIdException.class, () -> {
               orderService.reorderExistingOrder(UUID.randomUUID(),jwt);
            });
        }

        @Test
        void reorderExistingOrder_AuthStringPresentNoOrderID_returnsIllegalIdException(){
            Assertions.assertThrows(IllegalIdException.class,() -> {
               orderService.reorderExistingOrder(null,jwt);
            });
        }
    }

}