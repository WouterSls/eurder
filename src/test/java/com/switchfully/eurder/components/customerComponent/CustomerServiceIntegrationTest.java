package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Base64;
import java.util.UUID;

@SpringBootTest
public class CustomerServiceIntegrationTest {


    @Autowired
    ICustomerService customerService;


    final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("fooStreet", "0412345678");
    Jwt jwt = new Jwt("test", Instant.now(),Instant.now().plusSeconds(300),null,null);

    @Test
    void getCustomerUUIDFromAuth_AuthStringPresent_returnsCustomerUUID(){



        //when
        CustomerDTO testCustomer = customerService.createNewCustomer(jwt,TEST_CREATE_CUSTOMER_DTO);
        CustomerDTO TEST_CUSTOMER = customerService.getCustomerById(testCustomer.getId().toString());
        Assertions.assertNotNull(TEST_CUSTOMER);

        String userID = TEST_CUSTOMER.getId().toString();
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userID + ":password").getBytes());
        //TODO: fix test
    }

    @Test
    void getCustomerUUIDFromAuth_AuthStringInvalid_returnsIllegalArgumentIdFormat(){

        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(("user:password").getBytes());

        Assertions.assertThrows(IllegalIdException.class, () -> {
            customerService.getCustomerFromAuth(jwt);
        });
    }

    @Test
    void getCustomerUUIDFromAuth_AuthStringNotPresent_returnsMandatoryFieldException(){


        Assertions.assertThrows(MandatoryFieldException.class, () -> {
            customerService.getCustomerFromAuth(jwt);
        });
    }
}
