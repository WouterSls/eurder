package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.switchfully.eurder.utils.Feature.VIEW_ALL_CUSTOMERS;
import static com.switchfully.eurder.utils.Feature.VIEW_CUSTOMER_BY_ID;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final ICustomerService customerService;

    @Autowired
    public CustomerController(ICustomerService customerService){
        this.customerService = customerService;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path ="/create", produces="application/json",consumes = "application/json")
    CustomerDTO createNewCustomer(@RequestBody CreateCustomerDTO createCustomerDTO){
        return customerService.createNewCustomer(createCustomerDTO);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "create/admin", produces = "application/json",consumes = "application/json")
    CustomerDTO createNewAdmin(@RequestBody CreateCustomerDTO createCustomerDTO){
        return customerService.createNewAdmin(createCustomerDTO);
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public List<CustomerDTO> getAllCustomers(@RequestHeader String authorization){
        customerService.validateAuthorization(authorization,VIEW_ALL_CUSTOMERS);
        return customerService.getListCustomerDTO();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}", produces = "application/json")
    CustomerDTO getCustomerById(@PathVariable String id, @RequestHeader String authorization){
        customerService.validateAuthorization(authorization,VIEW_CUSTOMER_BY_ID);
        return customerService.getCustomerById(id);
    }
}