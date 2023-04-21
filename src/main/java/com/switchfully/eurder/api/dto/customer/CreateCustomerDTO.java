package com.switchfully.eurder.api.dto.customer;

public class CreateCustomerDTO{
    private final String address;
    private final String phoneNumber;

    public CreateCustomerDTO(String address, String phoneNumber) {
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

}