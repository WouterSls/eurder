package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrdersDTO;
import com.switchfully.eurder.api.dto.order.OrderDTO;
import com.switchfully.eurder.api.dto.order.itemGroup.OneOrderDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
import com.switchfully.eurder.components.orderComponent.IOrderService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;

import java.time.Instant;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderControllerSecurityTest {

    @LocalServerPort
    private int port;

    @Autowired
    IOrderService orderService;
    @Autowired
    IItemService itemService;
    @Autowired
    ICustomerService customerService;

    CustomerDTO customer, admin;

    CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar", "foo");

    Jwt jwt = new Jwt("test", Instant.now(),Instant.now().plusSeconds(300),null,null);

    String adminId, customerId;
    String adminPw = null;
    String customerPw = null;
    String testUUID = "a68631fa-d8f8-4dcc-b6f3-7e77e17207a5";

    Header header;

    @BeforeEach
    void setup() {

        customer = customerService.createNewCustomer(jwt,createCustomerDTO);
        customerId = customer.getId().toString();
        adminId = admin.getId().toString();
        header = new Header("Authorization", "Basic userId:password");
    }

    @Test
    void orderItems_CreateOrderDTONoOrderIdPresent_returns404() {

        final OneOrderDTO testItemGroup = new OneOrderDTO(null, 1);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));


        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .log().all()
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void orderItems_CreateOrderDTONoOrderAmount_returns404() {

        final OneOrderDTO testItemGroup = new OneOrderDTO(UUID.randomUUID(), 0);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void orderItems_CreateOrderDTOOrderPresentAmountBiggerThenStock_returns404() {


        ItemDTO createdItem = itemService.createNewItem(new CreateItemDTO("foo", "bar", 10, 5));

        final OneOrderDTO testItemGroup = new OneOrderDTO(createdItem.getId(), 10);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void orderItems_CreateOrderDTOPresent_returns201() {

        ItemDTO createdItem = itemService.createNewItem(new CreateItemDTO("foo", "bar", 10, 5));


        final OneOrderDTO testItemGroup = new OneOrderDTO(createdItem.getId(), 1);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }


    @Test
    void orderItems_CreateOrderDTOPresent_updatesItemAmount() {


        CreateItemDTO testItem = new CreateItemDTO("foo", "bar", 10, 5);


        ItemDTO gottenItem = itemService.createNewItem(testItem);

        final int amountToOrder = 1;

        final OneOrderDTO testItemGroup = new OneOrderDTO(gottenItem.getId(), 1);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());


        ItemDTO afterOrderItemDTO = itemService.getItemById(gottenItem.getId());


        Assertions.assertEquals((gottenItem.getAmount() - amountToOrder), afterOrderItemDTO.getAmount());
    }

    @Test
    void reportOrdersByCustomer_OrderAndCustomerPresent_returnsReportString() {

        CreateItemDTO testItem = new CreateItemDTO("foo", "bar", 10, 5);

        ItemDTO gottenItem = itemService.createNewItem(testItem);

        CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(new OneOrderDTO(gottenItem.getId(), 1)));

        String auth = "basic " + Base64.getEncoder().encodeToString((customerId + ":" + customerPw).getBytes());

        orderService.orderItems(testOrder,jwt);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .log().all()
                .when()
                .port(port)
                .get("/orders/my-orders")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void reorderExistingOrder_orderIdPresentAuthPresent_returns200() {


        CreateItemDTO testItem = new CreateItemDTO("foo", "bar", 10, 5);

        String auth = "basic " + Base64.getEncoder().encodeToString((customerId + ":" + customerPw).getBytes());

        ItemDTO gottenItem = itemService.createNewItem(testItem);

        final OneOrderDTO testItemGroup = new OneOrderDTO(gottenItem.getId(), 1);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        List<OrderDTO> gottenOrderList = orderService.orderItems(testOrder,jwt);

        OrderDTO order = gottenOrderList.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .log().all()
                .when()
                .port(port)
                .post("/orders/" + order.getId() + "/reorder")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void reorderExistingOrder_OrderIdNotPresentAuthPresent_returns404() {


        CreateItemDTO testItem = new CreateItemDTO("foo", "bar", 10, 5);

        String auth = "basic " + Base64.getEncoder().encodeToString((customerId + ":" + customerPw).getBytes());

        ItemDTO gottenItem = itemService.createNewItem(testItem);

        final OneOrderDTO testItemGroup = new OneOrderDTO(gottenItem.getId(), 1);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        List<OrderDTO> gottenOrderList = orderService.orderItems(testOrder, jwt);

        OrderDTO order = gottenOrderList.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .log().all()
                .when()
                .port(port)
                .post("/orders/" + UUID.randomUUID() + "/reorder")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void reorderExistingOrder_OrderIdPresentAuthPresentNotYourOrder_returns404() {


        CreateItemDTO testItem = new CreateItemDTO("foo", "bar", 10, 5);

        CustomerDTO testCustomerDTO = customerService.createNewCustomer(jwt,createCustomerDTO);
        String testCustomerId = testCustomerDTO.getId().toString();
        String testCustomerPassword = null;
        String auth = "basic " + Base64.getEncoder().encodeToString((customerId + ":" + customerPw).getBytes());

        ItemDTO gottenItem = itemService.createNewItem(testItem);

        final OneOrderDTO testItemGroup = new OneOrderDTO(gottenItem.getId(), 1);
        final CreateOrdersDTO testOrder = new CreateOrdersDTO(List.of(testItemGroup));

        List<OrderDTO> gottenOrderList = orderService.orderItems(testOrder, jwt);

        OrderDTO order = gottenOrderList.get(0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(testCustomerId, testCustomerPassword)
                .log().all()
                .when()
                .port(port)
                .post("/orders/" + order.getId() + "/reorder")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

}