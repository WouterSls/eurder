package com.switchfully.eurder;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.api.dto.order.itemGroup.CreateItemGroupDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.components.orderComponent.IOrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Base64;
import java.util.List;

@SpringBootTest
class EndToEndTest {


    @Autowired
    private IItemService itemService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICustomerService customerService;

    Jwt jwt = new Jwt("test", Instant.now(),Instant.now().plusSeconds(300),null,null);



    @Test
    @DisplayName("System integration test")
    void systemIntegrationTest() {
        //create a customer

        final CreateCustomerDTO newCustomer = new CreateCustomerDTO("fooStreet", "0412345678");

        CustomerDTO createdCustomer = customerService.createNewCustomer(jwt,newCustomer);
        Assertions.assertNotNull(createdCustomer);

        //test the customerService with the customer;
        final CustomerDTO customerTest1Actual = customerService.getCustomerById(createdCustomer.getId().toString());
        Assertions.assertEquals(createdCustomer, customerTest1Actual);
        final List<CustomerDTO> customerTest4Actual = customerService.getAllCustomers();
        Assertions.assertNotNull(customerTest4Actual);


        //create an Item
        final CreateItemDTO newItem = new CreateItemDTO("bar", "foo", 10, 5);
        ItemDTO createdItem = itemService.createNewItem(newItem);
        Assertions.assertNotNull(createdItem);

        //test the itemService with item
        final ItemDTO itemTest2Actual = itemService.findById(createdItem.getId());
        Assertions.assertEquals(createdItem, itemTest2Actual);
        final UpdateItemDTO updateItemDTO = new UpdateItemDTO("foo", "bar", 20, 10);
        final ItemDTO itemTest3Actual = itemService.updateItemById(updateItemDTO, createdItem.getId());
        Assertions.assertNotNull(itemTest3Actual);

        //create an order with above item and customer
        final CreateItemGroupDTO itemOrder = new CreateItemGroupDTO(createdItem.getId(), 1);
        final CreateOrderDTO createdOrder = new CreateOrderDTO(List.of(itemOrder));
        Assertions.assertNotNull(createdOrder);

        //test the orderService with the order
        String userId = createdCustomer.getId().toString();
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userId + ":password").getBytes());
        final List<OrderDTO> listOfOrders = orderService.orderItems(createdOrder, jwt);
        Assertions.assertNotNull(listOfOrders);
    }

}
