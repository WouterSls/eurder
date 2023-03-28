package com.switchfully.eurder.CustomerComponent;

import org.mockito.internal.matchers.GreaterThan;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
class CustomerRepository{

    private List<Customer> customers;

    private Customer testCustomer = new Customer("foo","bar","foobar@hotmail.com","fooStreetBar","fooNumberBar");

    CustomerRepository(){
        this.customers = new ArrayList<>();
        customers.add(testCustomer);
    }


    List<Customer> getCustomers() {
        return customers;
    }

    void addCustomer(Customer customer){
        customers.add(customer);
    }

    Optional<Customer> getCustomerByName(String name){
        return customers.stream()
                .filter(customer -> customer.getFirstName().equals(name) || customer.getLastName().equals(name))
                .findFirst();

    }


}