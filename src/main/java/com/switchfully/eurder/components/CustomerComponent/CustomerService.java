package com.switchfully.eurder.components.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.InvalidIdException;
import com.switchfully.eurder.exception.MandatoryFieldException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public List<CustomerDTO> getListCustomerDTO() {
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

        if (!isValidUUIDFormat(id)){
            throw new InvalidIdException("Please provide a correct user ID");
        }

        UUID customerID = UUID.fromString(id);
        return customerMapper.mapToDTO(customerRepository.getCustomerById(customerID).orElse(null));
    }


    private void validateRequiredFields(CreateCustomerDTO createCustomerDTO) throws MandatoryFieldException {
        if (createCustomerDTO == null) {
            throw new MandatoryFieldException("Please provide a customer to be created");
        }
        if (createCustomerDTO.getAddress() == null){
            throw new MandatoryFieldException("The address of the user is required");
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

    private boolean isValidUUIDFormat(String id){
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (id == null){
            return false;
        }
        return UUID_REGEX.matcher(id).matches();
    }

}