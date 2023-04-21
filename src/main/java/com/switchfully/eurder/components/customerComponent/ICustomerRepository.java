package com.switchfully.eurder.components.customerComponent;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ICustomerRepository extends JpaRepository<Customer, UUID> { }
