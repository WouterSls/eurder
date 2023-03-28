package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.CustomerComponent.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final ICustomerService customerService;

    @Autowired
    private CustomerController(ICustomerService customerService){
        this.customerService = customerService;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getListCustomerDTO();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path ="/create", produces="application/json",consumes = "application/json")
    CustomerDTO createNewCustomer(@RequestBody CreateCustomerDTO createCustomerDTO){
        return customerService.createNewCustomer(createCustomerDTO);
    }

}