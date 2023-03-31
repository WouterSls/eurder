package com.switchfully.eurder.components.securityComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;

public interface IUserService {

    void createUserFromCustomer(CustomerDTO customerDTO);
}
