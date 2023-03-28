package com.switchfully.eurder.CustomerComponent;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper){
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    public List<CustomerDTO> getListCustomerDTO(){
        return customerMapper.mapToDTO(customerRepository.getCustomers());
    }

    public CustomerDTO createNewCustomer(CreateCustomerDTO createCustomerDTO){
        Customer customerToBeAdded = customerMapper.mapToDomain(createCustomerDTO);
        customerRepository.addCustomer(customerToBeAdded);
        return customerMapper.mapToDTO(customerToBeAdded);

        //return customerMapper.mapToDTO(customerRepository.addCustomer(customerMapper.mapToDomain(createCustomerDTO)));
    }
}