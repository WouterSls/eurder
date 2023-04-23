package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

public interface ICustomerService {

    List<CustomerDTO> getAllCustomers();

    CustomerDTO createNewCustomer(Jwt jwt, CreateCustomerDTO createCustomerDTO);

    CustomerDTO getCustomerById(String id);

}
