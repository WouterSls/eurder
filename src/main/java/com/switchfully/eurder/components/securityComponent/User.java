package com.switchfully.eurder.components.securityComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;

import java.util.Objects;

public class User {
	private final CustomerDTO customer;
	private Role role;
	private String password;

	public User(CustomerDTO customer, Role role, String password) {
		this.customer = customer;
		this.role = role;
		this.password = password;
	}


	public CustomerDTO getCustomer() {
		return customer;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		User user = (User) o;
		return Objects.equals(customer.getId(), user.customer.getId());
	}

	@Override
	public int hashCode() {
		return Objects.hash(customer.getId());
	}

	public boolean hasAccessTo(Feature feature) {
		return role.hasFeature(feature);
	}

	public String getPassword() {
		return password;
	}

	public boolean doesPasswordMatch(String password) {
		return this.password.equals(password);
	}
}
