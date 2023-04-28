package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.exception.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
@Transactional
class CustomerService implements ICustomerService {

    private final ICustomerRepository customerRepository;

    @Autowired
    public CustomerService( ICustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Customer> getAllCustomers() {
        if (customerRepository.findAll().isEmpty()) {
            throw new NoCustomersException("Eurder currently holds 0 customers");
        }
        return customerRepository.findAll();
    }

    @Override
    public Customer createNewCustomer(Customer customer) {

        Customer savedCustomer = customerRepository.save(customer);
        return savedCustomer;

    }

    @Override
    public Customer getCustomerById(String id) {

        boolean wildcard = id.contains("*");

        if (wildcard) {
            return getCustomerWithWildcard(id);
        }

        return getCustomerWithoutWildcard(id);
    }

    @Override
    public Customer getCustomerFromAuth(Jwt jwt){
        return customerRepository.findAll().stream()
                .filter(customer -> customer.getEmailAddress().equals(jwt.getClaim("email")))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("no customer registered for this login"));
    }

    private Customer getCustomerWithWildcard(String id) {
        String customerIdWithoutWildcard = id.replace("*", "");

        Customer customerWithWildcard = customerRepository.findAll().stream()
                .filter(customer -> customer.getId().toString().contains(customerIdWithoutWildcard))
                .findFirst()
                .orElseThrow(() -> new IllegalIdException("No customer found for given ID"));

        return customerWithWildcard;
    }

    private Customer getCustomerWithoutWildcard(String id) {
        if (!isValidUUIDFormat(id)) {
            throw new InvalidIdFormatException("provide valid uuid format");
        }

        UUID customerID = UUID.fromString(id);

        Customer customerById = customerRepository.findById(customerID)
                .orElseThrow(() -> new IllegalIdException("No customer found for given ID"));

        return customerById;
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

}