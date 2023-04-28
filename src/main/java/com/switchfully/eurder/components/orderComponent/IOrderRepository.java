package com.switchfully.eurder.components.orderComponent;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface IOrderRepository extends JpaRepository<Order, UUID> {
}
