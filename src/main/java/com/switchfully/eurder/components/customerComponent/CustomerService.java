package com.switchfully.eurder.components.customerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.exception.*;
import com.switchfully.eurder.utils.Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
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
        if (customerRepository.getCustomers().isEmpty()){
            throw new NoCustomersException("Eurder currently holds 0 customers");
        }
        return customerMapper.mapToDTO(customerRepository.getCustomers());
    }

    @Override
    public CustomerDTO createNewCustomer(CreateCustomerDTO createCustomerDTO) {
        validateRequiredFields(createCustomerDTO);
        Customer newCustomer = customerMapper.mapToDomain(createCustomerDTO);
        customerRepository.addCustomer(newCustomer);
        return customerMapper.mapToDTO(newCustomer);

    }

    @Override
    public CustomerDTO createNewAdmin(CreateCustomerDTO createCustomerDTO){
        validateRequiredFields(createCustomerDTO);
        Customer newCustomer = customerMapper.mapToDomainAdmin(createCustomerDTO);
        customerRepository.addCustomer(newCustomer);
        return customerMapper.mapToDTO(newCustomer);
    }


    @Override
    public CustomerDTO getCustomerById(String id){

        boolean wildcard = id.contains("*");

        if (wildcard){
            return getCustomerWithWildcard(id);
        }

        return getCustomerWithoutWildcard(id);


    }

    @Override
    public CustomerDTO getCustomerFromAuth(String auth){

        if (auth == null){
            throw new MandatoryFieldException("provide auth");
        }

        UuidPassword uuidPassword;

        try{
            uuidPassword = getUuidPassword(auth);
        }catch (IllegalArgumentException e){
            throw new IllegalIdException("invalid authorization");
        }

        Customer customerById = customerRepository.getCustomerById(uuidPassword.getUuid())
                .orElseThrow(() ->  new UserNotFoundException("No user found with UUID: " + uuidPassword.getUuid()));

        return customerMapper.mapToDTO(customerById);
    }

    @Override
    public void validateAuthorization(String auth, Feature feature){

        if(auth == null){
            throw new UnauthorizedException("No authorization provided.");
        }

        UuidPassword uuidPassword;

        try {
            uuidPassword = getUuidPassword(auth);
        } catch(IllegalArgumentException exception){
            throw new IllegalIdException("No valid login UUID was provided.");
        }

        Customer customer = customerRepository.getCustomerById(uuidPassword.getUuid())
                .orElseThrow(() -> new UserNotFoundException("No user found with UUID: " + uuidPassword.getUuid()));

        if (!customer.doesPasswordMatch(uuidPassword.getPassword())) {
            throw new UnauthorizedException("Wrong password for user used.");
        }
        if (!customer.hasAccessTo(feature)) {
            throw new UnauthorizedException("User has no access to this feature.");
        }

    }


    private CustomerDTO getCustomerWithWildcard(String id){
        String customerIdWithoutWildcard = id.replace("*","");

        Customer customerWithWildcard = customerRepository.getCustomers().stream()
                .filter(customer -> customer.getId().toString().contains(customerIdWithoutWildcard))
                .findFirst()
                .orElseThrow(() -> new IllegalIdException("No customer found for given ID"));

        return customerMapper.mapToDTO(customerWithWildcard);
    }

    private CustomerDTO getCustomerWithoutWildcard(String id){
        if (!isValidUUIDFormat(id)){
            throw new InvalidIdFormatException("provide valid uuid format");
        }

        UUID customerID = UUID.fromString(id);

        Customer customerById = customerRepository.getCustomerById(customerID)
                .orElseThrow(() -> new IllegalIdException("No customer found for given ID"));

        return customerMapper.mapToDTO(customerById);
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
        if (createCustomerDTO.getPassword() == null || createCustomerDTO.getPassword().equals("")){
            throw new MandatoryFieldException("please provide a password");
        }
    }

    private UuidPassword getUuidPassword(String auth) {
        String decodedUsernamePassword = new String(Base64.getDecoder().decode(auth.substring("basic ".length())));
        UUID uuid = UUID.fromString(decodedUsernamePassword.substring(0, decodedUsernamePassword.indexOf(":")));
        String password = decodedUsernamePassword.substring(decodedUsernamePassword.indexOf(":") + 1);
        return new UuidPassword(uuid, password);
    }

    private static boolean isValidUUIDFormat(String id){
        Pattern UUID_REGEX =
                Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
        if (id == null){
            return false;
        }
        return UUID_REGEX.matcher(id).matches();
    }

}