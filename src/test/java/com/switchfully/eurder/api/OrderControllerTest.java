package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.order.CreateOrderDTO;
import com.switchfully.eurder.api.dto.order.ItemGroupDTO;
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
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class OrderControllerTest {

    CreateCustomerDTO customerDTO = new CreateCustomerDTO("foo", "bar", "foo", "bar", "foobar");
    CustomerDTO customer;
    Header header;

    @LocalServerPort
    private int port;

    @Autowired
    IOrderService orderService;
    @Autowired
    IItemService itemService;

    @BeforeEach()
    void addCustomerToService() {

        customer = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(customerDTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(CustomerDTO.class);

        header = new Header("Authorization", "Basic userId:password");
    }

    @Test
    void orderItems_CreateOrderDTONoOrderIdPresent_returns404() {

        final ItemGroupDTO testItemGroup = new ItemGroupDTO(null, 1);
        final CreateOrderDTO testOrder = new CreateOrderDTO(List.of(testItemGroup));


        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
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

        final ItemGroupDTO testItemGroup = new ItemGroupDTO("*4*", 0);
        final CreateOrderDTO testOrder = new CreateOrderDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void orderItems_CreateOrderDTOOrderPresentAmountBiggerThenStock_returns404() {


        itemService.createNewItem(new CreateItemDTO("foo", "bar", 10, 5));

        final ItemGroupDTO testItemGroup = new ItemGroupDTO("*4*", 10);
        final CreateOrderDTO testOrder = new CreateOrderDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
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


        final ItemGroupDTO testItemGroup = new ItemGroupDTO(createdItem.getId().toString(), 1);
        final CreateOrderDTO testOrder = new CreateOrderDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
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


        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testItem)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        final int amountToOrder = 1;

        final ItemGroupDTO testItemGroup = new ItemGroupDTO(gottenItem.getId().toString(), 1);
        final CreateOrderDTO testOrder = new CreateOrderDTO(List.of(testItemGroup));

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testOrder)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
                .when()
                .port(port)
                .post("/orders/order")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());


        ItemDTO afterOrderItemDTO =
                RestAssured.given()
                        .when()
                        .port(port)
                        .get("/items/" + gottenItem.getId())
                        .then()
                        .assertThat()
                        .statusCode(HttpStatus.OK.value())
                        .extract()
                        .as(ItemDTO.class);


        Assertions.assertEquals((gottenItem.getAmount() - amountToOrder), afterOrderItemDTO.getAmount());
    }

    @Test
    void reportOrdersByCustomer_OrderAndCustomerPresent_returnsReportString() {

        CreateItemDTO testItem = new CreateItemDTO("foo", "bar", 10, 5);


        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
                .body(testItem)
                .log().all()
                .when()
                .port(port)
                .post("items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(customer.getId().toString(), "password")
                .log().all()
                .when()
                .port(port)
                .get("/orders/my-order")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }
}