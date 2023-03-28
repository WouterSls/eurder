package com.switchfully.eurder.CustomerComponent;

class Address {
    private final String street;
    private final String houseNumber;
    private final String postalCode;
    private final String city;
    private final String country;

    Address(String street, String houseNumber, String postalCode, String city, String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.postalCode = postalCode;
        this.city = city;
        this.country = country;
    }

    String getStreet() {
        return street;
    }

    String getHouseNumber() {
        return houseNumber;
    }

    String getPostalCode() {
        return postalCode;
    }

    String getCity() {
        return city;
    }

    String getCountry() {
        return country;
    }
}
