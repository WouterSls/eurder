package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.utils.Feature;

import java.util.List;

public interface ICustomerService {

    List<CustomerDTO> getListCustomerDTO();

    CustomerDTO createNewCustomer(CreateCustomerDTO createCustomerDTO);

    CustomerDTO createNewAdmin(CreateCustomerDTO createCustomerDTO);

    CustomerDTO getCustomerById(String id);


    CustomerDTO getCustomerFromAuth(String auth);

    void validateAuthorization(String auth, Feature feature);
}
