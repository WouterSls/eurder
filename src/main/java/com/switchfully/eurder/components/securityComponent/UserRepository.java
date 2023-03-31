package com.switchfully.eurder.components.securityComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
class UserRepository {

	private List<User> userList;

	UserRepository() {
		this.userList = new ArrayList<>();
	}

	Optional<User> getUserByUuid(UUID id){
		return userList.stream()
				.filter(user -> user.getCustomer().getId().equals(id))
				.findFirst();
	}

	User addUser(User user) {
		userList.add(user);
		return user;
	}

}
