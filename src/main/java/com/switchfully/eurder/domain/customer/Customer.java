package com.switchfully.eurder.domain.customer;



public class Customer{
    private final String firstName;
    private final String lastName;
    private String emailAddress;
    private Address address;
    private String phoneNumber;

    public Customer(String firstName, String lastName, String emailAddress, Address address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}