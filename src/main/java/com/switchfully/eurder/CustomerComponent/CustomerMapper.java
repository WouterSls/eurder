package com.switchfully.eurder.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
class CustomerMapper {

    private AddressMapper addressMapper = new AddressMapper();

    List<CustomerDTO> mapToDTO(List<Customer> customerList){
        return customerList.stream()
                .map(this::mapToDTO)
                .toList();
    }

    CustomerDTO mapToDTO(Customer customer){
        return new CustomerDTO(customer.getFirstName(),
                customer.getLastName(),
                customer.getEmailAddress(),
                addressMapper.mapToDTO(customer.getAddress()),
                customer.getPhoneNumber());
    }

    Customer mapToDomain(CreateCustomerDTO createCustomerDTO){
        return new Customer(createCustomerDTO.getFirstName(), createCustomerDTO.getLastName(), createCustomerDTO.getEmailAddress(), null, createCustomerDTO.getPhoneNumber());
    }
}
