package com.switchfully.eurder;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.components.CustomerComponent.ICustomerService;
import com.switchfully.eurder.components.ItemComponent.IItemService;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
import com.switchfully.eurder.components.OrderComponent.IOrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class IntegrationTest {


    @Autowired
    private IItemService itemService;
    @Autowired
    private IOrderService orderService;
    @Autowired
    private ICustomerService customerService;



    @Test
    @DisplayName("Integration test for itemComponent in orderService")
    void systemIntegrationTest (){
        //create a customer
        CreateCustomerDTO newCustomer = new CreateCustomerDTO("foo","bar","foo@email.com","bar","0412345678");
        CustomerDTO createdCustomer = customerService.createNewCustomer(newCustomer);
        Assertions.assertNotNull(createdCustomer);

        //test the customerService with the customer;
        final CustomerDTO customerTest1Actual = customerService.getCustomerById(createdCustomer.getId().toString());
        Assertions.assertEquals(createdCustomer,customerTest1Actual);
        final CustomerDTO customerTest2Actual = customerService.getCustomerByName(createdCustomer.getFirstName());
        Assertions.assertNotNull(customerTest2Actual);
        final CustomerDTO customerTest3Actual = customerService.getCustomerByName(createdCustomer.getLastName());
        Assertions.assertNotNull(customerTest3Actual);
        final List<CustomerDTO> customerTest4Actual = customerService.getListCustomerDTO();
        Assertions.assertNotNull(customerTest4Actual);


        //create an Item
        final CreateItemDTO newItem = new CreateItemDTO("bar","foo",10,5);
        ItemDTO createdItem = itemService.createNewItem(newItem);
        Assertions.assertNotNull(createdItem);

        //test the itemService with item
        final ItemDTO itemTest1Actual = itemService.getItemByName(createdItem.getName());
        Assertions.assertEquals(createdItem,itemTest1Actual);
        final ItemDTO itemTest2Actual = itemService.getItemById(createdItem.getId().toString());
        Assertions.assertEquals(createdItem,itemTest2Actual);
        //TODO: add update functionality once it's done and customer save functionality

        //create an order with above item and customer
        final ItemGroupDTO itemOrder = new ItemGroupDTO(createdItem.getId().toString(),1);
        final CreateOrderDTO createdOrder = new CreateOrderDTO(List.of(itemOrder));
        Assertions.assertNotNull(createdOrder);

        //test the orderService with the order
        final List<OrderDTO> listOfOrders = orderService.orderItems(createdOrder);
        Assertions.assertNotNull(listOfOrders);
    }
}
