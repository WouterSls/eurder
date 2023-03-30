package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.item.CreateItemDTO;
import com.switchfully.eurder.api.dto.item.ItemDTO;
import com.switchfully.eurder.components.itemComponent.IItemService;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ResourceBanner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    IItemService itemService;

    private final CreateItemDTO TEST_CREATE_ITEM_DTO = new CreateItemDTO("foo","bar",10,5);

    @Test
    void getItemById_ItemNotPresent_returns404(){

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
    void getItemById_ItemPresentIncorrectId_returns404(){

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
    void getItemById_ItemPresent_returnsItemDTO(){

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

        Assertions.assertEquals(expectedItem,actualItem);
    }

    @Test
    void getListItemDTO_ItemsPresent_returnsItemDTOList(){

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
                .jsonPath().getList(".",ItemDTO.class);

        Assertions.assertNotNull(actualItemDTOList);
    }

    @Test
    void getListItemDTO_ItemsNotPresent_returns404(){

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
    void createItem_CreateItemDTOPresent_returns200(){

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
    void createItem_createItemDTONameNotPresent_returns404(){

        final CreateItemDTO testDTO = new CreateItemDTO(null,"bar",10,5);

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
    void createItem_createItemDTODescriptionNotPresent_returns404(){

        final CreateItemDTO testDTO = new CreateItemDTO("foo",null,10,5);

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
    void createItem_createItemDTOPriceEquals0_returns404(){

        final CreateItemDTO testDTO = new CreateItemDTO("foo","bar",0,5);

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
    void createItem_createItemDTOPriceUnder0_returns404(){

        final CreateItemDTO testDTO = new CreateItemDTO("foo","bar",-5,5);

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
    void createItem_createItemDTOAmountEquals0_returns404(){

        final CreateItemDTO testDTO = new CreateItemDTO("foo","bar",10,0);

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
    void createItem_createItemDTOAmountUnder0_returns404(){

        final CreateItemDTO testDTO = new CreateItemDTO("foo","bar",10,-5);

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
}