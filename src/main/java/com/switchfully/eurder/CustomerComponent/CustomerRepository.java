package com.switchfully.eurder.CustomerComponent;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
class CustomerRepository{

    private List<Customer> customers;

    Address testAddress = new Address("fooStreet","barNumber","fooPostal","barCity","foobarCountry");
    Customer testCustomer = new Customer("foo","bar","foobar@hotmail.com",testAddress,"fooNumberBar");

    CustomerRepository(){
        this.customers = new ArrayList<>();
        customers.add(testCustomer);
    }


    List<Customer> getCustomers() {
        return customers;
    }

    Customer addCustomer(Customer customer){
        customers.add(customer);
        return customer;
    }


}