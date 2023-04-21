package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class CustomerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    ICustomerService customerService;

    final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("fooStreet", "0412345678");
    CustomerDTO customer, admin;

    Jwt jwt = new Jwt("test", Instant.now(),Instant.now().plusSeconds(300),null,null);

    String adminId, customerId;
    String adminPw = null;
    String customerPw = null;
    String testUUID = "a68631fa-d8f8-4dcc-b6f3-7e77e17207a5";

    Header header;

    @BeforeEach
    void setup() {
        customer = customerService.createNewCustomer(jwt,TEST_CREATE_CUSTOMER_DTO);
        customerId = customer.getId().toString();
        adminId = admin.getId().toString();
        header = new Header("Authorization", "Basic userId:password");
    }


    @Test
    void getListCustomerDTO_customersPresentIncorrectAuthUUID_returns404IllegalIdException() {

        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(testUUID, "test")
                .when()
                .port(port)
                .get("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getListCustomerDTO_customersPresentInvalidAuthUUIDFormat_returns404InvalidIdFormatException() {
        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic("test", "test")
                .when()
                .port(port)
                .get("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getListCustomerDTO_customersPresentWrongAuthPw_returns404UnauthorizedException() {

        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(adminId, "test")
                .when()
                .port(port)
                .get("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getListCustomerDTO_customersPresentIllegalRole_returns404UnauthorizedException() {

        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .when()
                .port(port)
                .get("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getListCustomerDTO_customersPresent_returns200CustomerDTOList() {


        List<CustomerDTO> actualCustomerDTOList = RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(adminId, adminPw)
                .when()
                .port(port)
                .get("/customers")
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .jsonPath().getList(".", CustomerDTO.class);

        Assertions.assertEquals(List.of(admin, customer), actualCustomerDTOList);
    }

    @Test
    void getCustomerById_customerPresentInvalidCustomerIdFormat_returns404InvalidIdFormatException() {
        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(adminId, adminPw)
                .when()
                .port(port)
                .get("/customers/1")
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getCustomerById_customerPresentInvalidCustomerId_returns404IllegalIdException() {
        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(adminId, adminPw)
                .when()
                .port(port)
                .get("customers/" + testUUID)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getCustomerById_customerPresentIllegalRole_returns404UnauthorizedException() {

        RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(customerId, customerPw)
                .when()
                .port(port)
                .get("/customers/" + customerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    void getCustomerById_customerPresent_returns200CustomerDTO() {

        CustomerDTO actualCustomer = RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(adminId, adminPw)
                .when()
                .port(port)
                .get("/customers/" + customerId)
                .then()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerDTO.class);

        Assertions.assertEquals(customer, actualCustomer);
    }

    @Test
    void getCustomerById_customerPresentWildCardId_returns200CustomerDTO() {

        String customerIdWithWildcard = customerId.substring(0, customerId.length() - 5).concat("*");

        CustomerDTO actualCustomer = RestAssured
                .given()
                .header(header)
                .auth().preemptive().basic(adminId, adminPw)
                .when()
                .port(port)
                .get("/customers/" + customerIdWithWildcard)
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerDTO.class);

        Assertions.assertEquals(customer, actualCustomer);
    }

    @Test
    void createNewCustomer_createCustomerDTOFirstNameNotPresent_returns404() {

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("fooStreet","04123789");

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
    void createNewCustomer_createCustomerDTOLastNameNotPresent_returns404() {

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("fizz","011808080");

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
    void createNewCustomer_createCustomerDTOEmailAddressNotPresent_returns404() {

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("fizz","011808080");


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
    void createNewCustomer_createCustomerDTOAddressNotPresent_returns404() {

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("fizz","011808080");

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
    void createNewCustomer_createCustomerDTOPhoneNumberNotPresent_returns404() {



        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar", "foo");

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
    void createNewCustomer_createCustomerDTOPasswordNotPresent_returns404() {

        CreateCustomerDTO createCustomerDTO = new CreateCustomerDTO("bar", "foo");

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
    void createNewCustomer_createCustomerDTOPresent_returns200CustomerDTO() {

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