package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.IllegalIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.UUID;

@SpringBootTest
public class CustomerServiceIntegrationTest {


    @Autowired
    ICustomerService customerService;





    @Test
    void getCustomerUUIDFromAuth_AuthStringPresent_returnsCustomerUUID(){

        //given
        final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("foo","bar","fizz","buzz","foobarfizzbuzz","test");

        //when
        CustomerDTO testCustomer = customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);
        CustomerDTO TEST_CUSTOMER = customerService.getCustomerById(testCustomer.getId().toString());
        Assertions.assertNotNull(TEST_CUSTOMER);

        String userID = TEST_CUSTOMER.getId().toString();
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userID + ":password").getBytes());
        CustomerDTO actualCustomer = customerService.getCustomerFromAuth(encodedAuth);
        Assertions.assertEquals(TEST_CUSTOMER,actualCustomer);
    }

    @Test
    void getCustomerUUIDFromAuth_AuthStringInvalid_returnsIllegalArgumentIdFormat(){

        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString(("user:password").getBytes());

        Assertions.assertThrows(IllegalIdException.class, () -> {
            customerService.getCustomerFromAuth(encodedAuth);
        });
    }

    @Test
    void getCustomerUUIDFromAuth_AuthStringNotPresent_returnsMandatoryFieldException(){

        String encodedAuth = null;

        Assertions.assertThrows(MandatoryFieldException.class, () -> {
            customerService.getCustomerFromAuth(encodedAuth);
        });
    }
}
