package com.switchfully.eurder.components.securityComponent;

import com.switchfully.eurder.api.dto.customer.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
class UserService implements IUserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void createUserFromCustomer(CustomerDTO customerDTO){
        User userToBeAdded = new User(customerDTO,Role.CUSTOMER,"test");
        userRepository.addUser(userToBeAdded);
    }
}
