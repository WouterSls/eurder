package com.switchfully.eurder.components.securityComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import com.switchfully.eurder.exception.InvalidIdFormatException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SecurityServiceTest {

    @Autowired
    ICustomerService customerService;
    @Autowired
    ISecurityService securityService;


    @Test
    void getCustomerUUIDFromAuth_AuthStringPresent_returnsCustomerUUID(){

        //given
        final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("foo","bar","fizz","buzz","foobarfizzbuzz");

        //when
        customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);
        CustomerDTO TEST_CUSTOMER = customerService.getCustomerByName("foo");
        Assertions.assertNotNull(TEST_CUSTOMER);

        String userID = TEST_CUSTOMER.getId().toString();
        String encodedAuth = "Basic " + Base64.getEncoder().encodeToString((userID + ":password").getBytes());

        UUID customerId = securityService.getCustomerUUIDFromAuth(encodedAuth);
        Assertions.assertNotNull(customerId);

        CustomerDTO actualCustomer = customerService.getCustomerById(customerId.toString());
        Assertions.assertEquals(TEST_CUSTOMER,actualCustomer);
    }

    @Test
    void getCustomerUUIDFromAuth_AuthStringInvalid_returnsInvalidIdFormat(){
        final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("foo","bar","fizz","buzz","foobarfizzbuzz");

        //when
        customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);
        CustomerDTO TEST_CUSTOMER = customerService.getCustomerByName("foo");
        Assertions.assertNotNull(TEST_CUSTOMER);

        String userID = TEST_CUSTOMER.getId().toString();
            String encodedAuth = "Basic username:password";

        Assertions.assertThrows(InvalidIdFormatException.class, () -> {
            UUID customerId = securityService.getCustomerUUIDFromAuth(encodedAuth);
        });
    }

    @Test
    void getCustomerUUIDFromAuth_AuthStringNotPresent_returnsMandatoryFieldException(){
        final CreateCustomerDTO TEST_CREATE_CUSTOMER_DTO = new CreateCustomerDTO("foo","bar","fizz","buzz","foobarfizzbuzz");

        //when
        customerService.createNewCustomer(TEST_CREATE_CUSTOMER_DTO);
        CustomerDTO TEST_CUSTOMER = customerService.getCustomerByName("foo");
        Assertions.assertNotNull(TEST_CUSTOMER);

        String encodedAuth = null;

        Assertions.assertThrows(MandatoryFieldException.class, () -> {
            UUID customerId = securityService.getCustomerUUIDFromAuth(encodedAuth);
        });
    }
}