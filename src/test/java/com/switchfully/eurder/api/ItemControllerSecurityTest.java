package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.components.itemComponent.IItemService;
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
import java.util.List;
import java.util.UUID;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerSecurityTest {
    @LocalServerPort
    private int port;

    @Autowired
    IItemService itemService;
    @Autowired
    ICustomerService customerService;

    final CreateItemDTO TEST_CREATE_ITEM_DTO = new CreateItemDTO("foo", "bar", 10, 5);
    CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar", "foo");
    CustomerDTO customer, admin;

    Jwt jwt = new Jwt("test", Instant.now(),Instant.now().plusSeconds(300),null,null);

    String adminId, customerId;

    String testUUID = "a68631fa-d8f8-4dcc-b6f3-7e77e17207a5";
    String adminPw = null;

    Header header;

    @BeforeEach
    void setup() {
        customer = customerService.createNewCustomer(jwt,createCustomerDTO);
        customerId = customer.getId().toString();
        adminId = admin.getId().toString();
        header = new Header("Authorization", "Basic userId:password");
    }

    @Test
    void createItem_CreateItemDTOPresent_returns200() {

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void createItem_createItemDTONameNotPresent_returns404() {

        final CreateItemDTO testDTO = new CreateItemDTO(null, "bar", 10, 5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testDTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createItem_createItemDTODescriptionNotPresent_returns404() {

        final CreateItemDTO testDTO = new CreateItemDTO("foo", null, 10, 5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testDTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createItem_createItemDTOPriceEquals0_returns404() {

        final CreateItemDTO testDTO = new CreateItemDTO("foo", "bar", 0, 5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testDTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createItem_createItemDTOPriceUnder0_returns404() {

        final CreateItemDTO testDTO = new CreateItemDTO("foo", "bar", -5, 5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testDTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createItem_createItemDTOAmountEquals0_returns404() {

        final CreateItemDTO testDTO = new CreateItemDTO("foo", "bar", 10, 0);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testDTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createItem_createItemDTOAmountUnder0_returns404() {

        final CreateItemDTO testDTO = new CreateItemDTO("foo", "bar", 10, -5);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testDTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_UpdateItemDTOPresent_returns200() {

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("bar", "foo", 20, 10);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testUpdateDTO)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void updateItem_ITemDTOPresentNoNamePresent_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO(null, "foo", 20, 10);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_ITemDTOPresentNoDescriptionPresent_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("foo", null, 20, 10);

        ItemDTO gottenItem =itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_ITemDTOPresentAmountEquals0_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("bar", "foo", 0, 10);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_ITemDTOPresentAmountUnder0_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("bar", "foo", -5, 10);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_ITemDTOPresentPriceEquals0_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("bar", "foo", 20, 0);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_ITemDTOPresentPriceUnder0_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("bar", "foo", 20, -5);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void updateItem_ITemDTOPresentIncorrectID_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO("bar", "foo", 20, -5);

        ItemDTO gottenItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + UUID.randomUUID() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getSortedItemStock_ItemsNotPresent_returns404NoItemsException(){
        RestAssured.given()
                .when()
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .port(port)
                .get("/items/stock")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getSortedItemStock_ItemsPresent_returnsSortedItemDTOList(){

        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .port(port)
                .get("items/stock")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem2,expectedItem3,expectedItem1),actualList);
    }

    @Test
    void getItemsStockByUrgency_ItemsNotPresent_returns404NoItemsException(){
        RestAssured.given()
                .when()
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .port(port)
                .get("/items/stock/low")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getItemsStockByStockLevel_ItemsPresentLowUrgency_returnsListOfHighStockItems(){
        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .port(port)
                .get("items/stock/low")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem1),actualList);
    }

    @Test
    void getItemsStockByStockLevel_ItemsPresentMediumUrgency_returnsListOfMediumStockItems(){
        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .port(port)
                .get("items/stock/medium")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem3),actualList);
    }

    @Test
    void getItemsStockByStockLevel_ItemsPresentHighUrgency_returnsListOfLowStockItems(){
        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .header(header)
                .auth().preemptive().basic(adminId,adminPw)
                .port(port)
                .get("items/stock/high")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem2),actualList);
    }
}