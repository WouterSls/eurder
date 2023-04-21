package com.switchfully.eurder.api;

import com.switchfully.eurder.api.dto.customer.CreateCustomerDTO;
import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import com.switchfully.eurder.components.customerComponent.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "customers")
public class CustomerController {

    private final ICustomerService customerService;

    @Autowired
    public CustomerController(ICustomerService customerService){
        this.customerService = customerService;
    }

    @PreAuthorize("hasAuthority('member')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path ="/register", produces="application/json",consumes = "application/json")
    CustomerDTO createNewCustomer(@AuthenticationPrincipal Jwt jwt, @RequestBody CreateCustomerDTO createCustomerDTO){
        return customerService.createNewCustomer(jwt,createCustomerDTO);
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(produces = "application/json")
    public List<CustomerDTO> getAllCustomers(){
        return customerService.getAllCustomers();
    }

    @PreAuthorize("hasAuthority('manager')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(path = "/{id}", produces = "application/json")
    CustomerDTO getCustomerById(@PathVariable String id){
        return customerService.getCustomerById(id);
    }
}