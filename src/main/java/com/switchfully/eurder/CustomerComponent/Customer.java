package com.switchfully.eurder.CustomerComponent;



class Customer{
    private String firstName;
    private String lastName;
    private String emailAddress;
    private Address address;
    private String phoneNumber;

    Customer(String firstName, String lastName, String emailAddress, Address address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    String getEmailAddress() {
        return emailAddress;
    }

    Address getAddress() {
        return address;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }
}