package com.switchfully.eurder.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;

import java.util.List;

public interface ICustomerService {

    List<CustomerDTO> getListCustomerDTO();

    CustomerDTO createNewCustomer(CreateCustomerDTO createCustomerDTO);
}
