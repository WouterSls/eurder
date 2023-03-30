package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.InvalidIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import com.switchfully.eurder.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final Utils utils;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
        this.utils = new Utils();
    }

    @Override
    public List<CustomerDTO> getListCustomerDTO() {
        if (customerRepository.getCustomers().isEmpty()){
            throw new MandatoryFieldException("Eurder currently holds 0 customers");
        }
        return customerMapper.mapToDTO(customerRepository.getCustomers());
    }

    @Override
    public CustomerDTO createNewCustomer(CreateCustomerDTO createCustomerDTO) {
        validateRequiredFields(createCustomerDTO);
        Customer customerToBeAdded = customerMapper.mapToDomain(createCustomerDTO);
        customerRepository.addCustomer(customerToBeAdded);
        return customerMapper.mapToDTO(customerToBeAdded);

    }

    @Override
    public CustomerDTO getCustomerByName(String name) {
        return customerMapper.mapToDTO(customerRepository.getCustomerByName(name).orElse(null));
    }

    @Override
    public CustomerDTO getCustomerById(String id){
        boolean wildcard = id.contains("*");

        if (wildcard){
            String customerIdWithoutWildcard = id.replace("*","");
            Optional<Customer> customerWithWildcard = customerRepository.getCustomers().stream()
                    .filter(customer -> customer.getId().toString().contains(customerIdWithoutWildcard))
                    .findFirst();
            return customerMapper.mapToDTO(customerWithWildcard.orElse(null));
        }

        if (!utils.isValidUUIDFormat(id)){
            throw new InvalidIdException("Please provide a correct user ID");
        }

        UUID customerID = UUID.fromString(id);
        return customerMapper.mapToDTO(customerRepository.getCustomerById(customerID).orElse(null));
    }


    private void validateRequiredFields(CreateCustomerDTO createCustomerDTO) throws MandatoryFieldException {

        if (createCustomerDTO == null) {
            throw new MandatoryFieldException("Please provide a customer to be created");
        }
        if (createCustomerDTO.getAddress() == null || createCustomerDTO.getAddress().equals("")){
            throw new MandatoryFieldException("The address of the user is required");
        }
        if (createCustomerDTO.getFirstName() == null || createCustomerDTO.getFirstName().equals("")){
            throw new MandatoryFieldException("The firstname of the customer is required");
        }
        if (createCustomerDTO.getLastName() == null ||createCustomerDTO.getLastName().equals("")){
            throw new MandatoryFieldException("The lastname of the customer is required");
        }
        if (createCustomerDTO.getEmailAddress() == null || createCustomerDTO.getEmailAddress().equals("") ){
            throw new MandatoryFieldException("The email address of the customer is required");
        }
        if (createCustomerDTO.getPhoneNumber() == null || createCustomerDTO.getPhoneNumber().equals("")) {
            throw new MandatoryFieldException("The phone number of the customer is required");
        }
    }


}