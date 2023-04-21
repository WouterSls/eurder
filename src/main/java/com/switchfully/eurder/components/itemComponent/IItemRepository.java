package com.switchfully.eurder.components.itemComponent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IItemRepository extends JpaRepository<Item, UUID> {
}
