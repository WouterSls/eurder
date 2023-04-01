package com.switchfully.eurder.components.customerComponent;


import com.switchfully.eurder.utils.Feature;

import java.util.Objects;
import java.util.UUID;

class Customer{
    private final UUID id;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String address;
    private String phoneNumber;
    private Role role;
    private final String password;

    Customer(String firstName, String lastName, String emailAddress, String address, String phoneNumber, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.id = UUID.randomUUID();
        this.role = role;
        this.password = password;
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

    String getAddress() {
        return address;
    }

    String getPhoneNumber() {
        return phoneNumber;
    }

    UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return Objects.equals(getFirstName(), customer.getFirstName()) && Objects.equals(getLastName(), customer.getLastName()) && Objects.equals(getEmailAddress(), customer.getEmailAddress()) && Objects.equals(getAddress(), customer.getAddress()) && Objects.equals(getPhoneNumber(), customer.getPhoneNumber());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFirstName(), getLastName(), getEmailAddress(), getAddress(), getPhoneNumber());
    }

    boolean hasAccessTo(Feature feature) {
        return role.hasFeature(feature);
    }

    String getPassword() {
        return password;
    }

    boolean doesPasswordMatch(String password) {
        return this.password.equals(password);
    }
}