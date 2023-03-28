package com.switchfully.eurder.dto.customer;


class CustomerDTO{
    private final String firstName;
    private final String lastName;
    private final String emailAddress;
    private final AddressDTO address;
    private final String phoneNumber;

    public CustomerDTO(String firstName, String lastName, String emailAddress, AddressDTO address, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }
}