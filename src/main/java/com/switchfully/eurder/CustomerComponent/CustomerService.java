package com.switchfully.eurder.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getListCustomerDTO() {
        return customerMapper.mapToDTO(customerRepository.getCustomers());
    }

    public CustomerDTO createNewCustomer(CreateCustomerDTO createCustomerDTO) {
        validateRequiredFields(createCustomerDTO);
        Customer customerToBeAdded = customerMapper.mapToDomain(createCustomerDTO);
        customerRepository.addCustomer(customerToBeAdded);
        return customerMapper.mapToDTO(customerToBeAdded);

    }

    public CustomerDTO getCustomerByName(String name) {
        return customerMapper.mapToDTO(customerRepository.getCustomerByName(name).orElse(null));
    }


    private void validateRequiredFields(CreateCustomerDTO createCustomerDTO) throws MandatoryFieldException {
        if (createCustomerDTO == null) {
            throw new MandatoryFieldException("Please provide a customer to be created");
        }
        if (createCustomerDTO.getAddress() == null){
            throw new MandatoryFieldException("The address of the user can't be null");
        }
        if (createCustomerDTO.getFirstName() == null){
            throw new MandatoryFieldException("The firstname of the customer is required");
        }
        if (createCustomerDTO.getLastName() == null){
            throw new MandatoryFieldException("The lastname of the customer is required");
        }
        if (createCustomerDTO.getEmailAddress() == null ){
            throw new MandatoryFieldException("The email address of the customer is required");
        }
        if (createCustomerDTO.getPhoneNumber() == null) {
            throw new MandatoryFieldException("The phone number of the customer is required");
        }
    }


}