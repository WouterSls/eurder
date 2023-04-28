package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ICustomerService {

    List<Customer> getAllCustomers();

    Customer createNewCustomer(Customer customer);

    Customer getCustomerById(String id);

    Customer getCustomerFromAuth(Jwt jwt);

}
