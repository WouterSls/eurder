package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class CustomerMapper {

    List<CustomerDTO> mapToDTO(List<Customer> customerList){
        return customerList.stream()
                .map(this::mapToDTO)
                .toList();
    }

    CustomerDTO mapToDTO(Customer customer){
        return new CustomerDTO(customer.getFirstName(),
                customer.getLastName(),
                customer.getEmailAddress(),
                customer.getAddress(),
                customer.getPhoneNumber(),
                customer.getId());
    }

    Customer mapToDomain(Jwt jwt, CreateCustomerDTO createCustomerDTO){
        return new Customer(UUID.randomUUID(),
                jwt.getClaim("given_name"),
                jwt.getClaim("family_name"),
                jwt.getClaim("email"),
                createCustomerDTO.getAddress(),
                createCustomerDTO.getPhoneNumber());
    }

}
