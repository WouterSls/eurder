package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.api.dto.item.UpdateItemDTO;
import com.switchfully.eurder.components.itemComponent.IItemService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    IItemService itemService;

    private final CreateItemDTO TEST_CREATE_ITEM_DTO = new CreateItemDTO("foo", "bar", 10, 5);

    @Test
    void getItemById_ItemNotPresent_returns404() {

        RestAssured
                .given()
                .when()
                .port(port)
                .get("/items/*1*")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getItemById_ItemPresentIncorrectId_returns404() {

        itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        RestAssured
                .given()
                .when()
                .port(port)
                .get("items/1")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getItemById_ItemPresent_returnsItemDTO() {

        ItemDTO expectedItem = itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        ItemDTO actualItem = RestAssured
                .given()
                .when()
                .port(port)
                .get("/items/" + expectedItem.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(ItemDTO.class);

        Assertions.assertEquals(expectedItem, actualItem);
    }

    @Test
    void getListItemDTO_ItemsPresent_returnsItemDTOList() {

        itemService.createNewItem(TEST_CREATE_ITEM_DTO);

        List<ItemDTO> actualItemDTOList = RestAssured
                .given()
                .when()
                .port(port)
                .get("/items")
                .then()
                .log().all()
                .assertThat().
                statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", ItemDTO.class);

        Assertions.assertNotNull(actualItemDTOList);
    }

    @Test
    void getListItemDTO_ItemsNotPresent_returns404() {

        RestAssured
                .given()
                .when()
                .port(port)
                .get("/items")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createItem_CreateItemDTOPresent_returns200() {

        RestAssured.given()
                .contentType(ContentType.JSON)
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


        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);


        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + gottenItem.getId() + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value());
    }

    @Test
    void updateItem_ITemDTOPresentNoNamePresent_returns404(){

        final UpdateItemDTO testUpdateDTO = new UpdateItemDTO(null, "foo", 20, 10);

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
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

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
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

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
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

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
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

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
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

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
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

        ItemDTO gottenItem = RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_ITEM_DTO)
                .when()
                .port(port)
                .post("/items/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .as(ItemDTO.class);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(testUpdateDTO)
                .when()
                .port(port)
                .put("/items/" + "1234" + "/update")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getSortedItemStock_ItemsNotPresent_returns404NoItemsException(){
        RestAssured.given()
                .when()
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
                .port(port)
                .get("items/stock")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".",ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem2,expectedItem3,expectedItem1),actualList);
    }

    @Test
    void getItemsStockByUrgency_ItemsNotPresent_returns404NoItemsException(){
        RestAssured.given()
                .when()
                .port(port)
                .get("/items/stock/low")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getItemsStockByStockLevel_ItemsPresentLowStock_returnsListOfLowStockItems(){
        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .port(port)
                .get("items/stock/low")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".",ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem2),actualList);
    }

    @Test
    void getItemsStockByStockLevel_ItemsPresentMediumStock_returnsListOfMediumStockItems(){
        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .port(port)
                .get("items/stock/medium")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".",ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem3),actualList);
    }

    @Test
    void getItemsStockByStockLevel_ItemsPresentHighStock_returnsListOfHighStockItems(){
        CreateItemDTO testCreateItem = new CreateItemDTO("foo", "bar", 10, 11);
        CreateItemDTO testCreateItem2 = new CreateItemDTO("fizz", "buzz", 20, 3);
        CreateItemDTO testCreateItem3 = new CreateItemDTO("foobar","fizzbuzz",30, 7);
        ItemDTO expectedItem1 = itemService.createNewItem(testCreateItem);
        ItemDTO expectedItem2 = itemService.createNewItem(testCreateItem2);
        ItemDTO expectedItem3 = itemService.createNewItem(testCreateItem3);

        List<ItemDTO> actualList = RestAssured.given()
                .when()
                .port(port)
                .get("items/stock/high")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".",ItemDTO.class);

        Assertions.assertEquals(List.of(expectedItem1),actualList);
    }
}