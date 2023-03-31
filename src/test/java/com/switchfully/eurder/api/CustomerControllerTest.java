package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
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
class CustomerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    ICustomerService customerService;

    final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("foo", "bar", "foo@email.com", "bar", "041234567");
    CustomerDTO expectedCustomerDTO;


    @Test
    void retrieveUserById_userNotPresent_returns404() {
        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/customers/1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    @Test
    void getListCustomerDTO_usersNotPresent_returns404(){

        RestAssured
                .given()
                .when()
                .port(port)
                .get("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }


    @Test
    void retrieveUserById_userPresent_returnsUser() {

        expectedCustomerDTO = customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);

        CustomerDTO actualCustomerDTO = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .port(port)
                .get("/customers/" + expectedCustomerDTO.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerDTO.class);

        Assertions.assertEquals(expectedCustomerDTO, actualCustomerDTO);
    }

    @Test
    void retrieveUserById_userPresentIncorrectId_returns404() {

        expectedCustomerDTO = customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);

        RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/customers/1")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void retrieveUserById_UserPresentWildCardId_returnsCustomerDTO(){


    RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_CUSTOMER_DTO)
                .when()
                .port(port)
                .post("customers/create")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());



        CustomerDTO actualCustomerDTO = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/customers/*")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerDTO.class);

        Assertions.assertNotNull(actualCustomerDTO);
    }

    @Test
    void getListCustomerDTO_userPresent_returnsCustomerDTOList(){

        customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);

        List<CustomerDTO> actualCustomerDTOList = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .when()
                .port(port)
                .get("/customers")
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", CustomerDTO.class);

        Assertions.assertNotNull(actualCustomerDTOList);
    }

    @Test
    void createCustomer_CreateCustomerDTOFirstNameNotPresent_returns404(){

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO(null,"foo","bar","foobar","041234567");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createCustomerDTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createCustomer_CreateCustomerDTOLastNameNotPresent_returns404(){

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("foo",null,"bar","foobar","041234567");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createCustomerDTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createCustomer_CreateCustomerDTOEmailAddressNotPresent_returns404(){

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar","foo",null,"foobar","041234567");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createCustomerDTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createCustomer_CreateCustomerDTOAddressNotPresent_returns404(){

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar","foo","bar",null,"041234567");

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createCustomerDTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createCustomer_CreateCustomerDTOPhoneNumberNotPresent_returns404(){

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar","foo","bar","foobar",null);

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(createCustomerDTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void createCustomer_CreateCustomerPresent_returns200(){

        RestAssured.given()
                .contentType(ContentType.JSON)
                .body(TEST_CREATE_CUSTOMER_DTO)
                .when()
                .port(port)
                .post("/customers/create")
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());

    }
}