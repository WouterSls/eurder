package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.stereotype.Component;

import java.util.List;

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

    Customer mapToDomain(CreateCustomerDTO createCustomerDTO){
        return new Customer(createCustomerDTO.getFirstName(), createCustomerDTO.getLastName(), createCustomerDTO.getEmailAddress(), createCustomerDTO.getAddress(), createCustomerDTO.getPhoneNumber());
    }
}
