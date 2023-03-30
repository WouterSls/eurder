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


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class CustomerControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    ICustomerService customerService;

    CreateCustomerDTO customerToBeCreated = new CreateCustomerDTO("foo","bar","foo@email.com","bar","041234567");

    @Test
    void userPresent_retrieveUserById_returnsUser(){
        CustomerDTO customerDTO = customerService.createNewCustomer(customerToBeCreated);

        CustomerDTO customer = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .port(port)
                .get("/customers/" + customerDTO.getId())
                .then()
                .log().all()
                .assertThat()
                .statusCode(HttpStatus.OK.value())
                .extract()
                .as(CustomerDTO.class);

        Assertions.assertEquals(customerDTO,customer);
    }

}