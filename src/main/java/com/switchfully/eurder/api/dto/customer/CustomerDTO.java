package com.switchfully.eurder.api.dto.customer;


public class CustomerDTO{
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final String address;
    private final String phoneNumber;

    public CustomerDTO(String firstName, String lastName, String emailAddress, String address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }
}