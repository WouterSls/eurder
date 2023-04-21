package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
class CustomerService implements ICustomerService {

    private final CustomerMapper customerMapper;
    private final ICustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerMapper customerMapper, ICustomerRepository customerRepository) {
        this.customerMapper = customerMapper;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        if (customerRepository.findAll().isEmpty()) {
            throw new NoCustomersException("Eurder currently holds 0 customers");
        }
        return customerMapper.mapToDTO(customerRepository.findAll());
    }

    @Override
    public CustomerDTO createNewCustomer(Jwt jwt, CreateCustomerDTO createCustomerDTO) {

        validateRequiredFields(createCustomerDTO);
        validateAuth(jwt);

        Customer newCustomer = customerMapper.mapToDomain(jwt,createCustomerDTO);
        customerRepository.save(newCustomer);
        return customerMapper.mapToDTO(newCustomer);

    }

    @Override
    public CustomerDTO getCustomerById(String id) {

        boolean wildcard = id.contains("*");

        if (wildcard) {
            return getCustomerWithWildcard(id);
        }

        return getCustomerWithoutWildcard(id);


    }

    private CustomerDTO getCustomerWithWildcard(String id) {
        String customerIdWithoutWildcard = id.replace("*", "");

        Customer customerWithWildcard = customerRepository.findAll().stream()
                .filter(customer -> customer.getId().toString().contains(customerIdWithoutWildcard))
                .findFirst()
                .orElseThrow(() -> new IllegalIdException("No customer found for given ID"));

        return customerMapper.mapToDTO(customerWithWildcard);
    }

    private CustomerDTO getCustomerWithoutWildcard(String id) {
        if (!isValidUUIDFormat(id)) {
            throw new InvalidIdFormatException("provide valid uuid format");
        }

        UUID customerID = UUID.fromString(id);

        Customer customerById = customerRepository.findById(customerID)
                .orElseThrow(() -> new IllegalIdException("No customer found for given ID"));

        return customerMapper.mapToDTO(customerById);
    }

    private void validateRequiredFields(CreateCustomerDTO createCustomerDTO) throws MandatoryFieldException {

        if (createCustomerDTO == null) {
            throw new MandatoryFieldException("Please provide a customer to be created");
        }
        if (createCustomerDTO.getAddress() == null || createCustomerDTO.getAddress().equals("")) {
            throw new MandatoryFieldException("The address of the user is required");
        }
        if (createCustomerDTO.getPhoneNumber() == null || createCustomerDTO.getPhoneNumber().equals("")) {
            throw new MandatoryFieldException("The phone number of the customer is required");
        }
    }
    private void validateAuth(Jwt jwt){
        boolean customerExists = customerRepository.findAll().stream()
                .anyMatch(customer -> customer.getEmailAddress().equals(jwt.getClaim("email")));
        if (customerExists){
            throw new IllegalArgumentException("customer already exists, please login");
        }
    }

    private static boolean isValidUUIDFormat(String id) {
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (id == null) {
            return false;
        }
        return UUID_REGEX.matcher(id).matches();
    }

    public CustomerDTO getCustomerFromAuth(Jwt jwt){

        Customer customerFromAuth = customerRepository.findAll().stream()
                .filter(customer -> customer.getEmailAddress().equals(jwt.getClaim("email")))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No customer from auth, please register"));

        return customerMapper.mapToDTO(customerFromAuth);
    }

}