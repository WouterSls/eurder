package com.switchfully.eurder.components.itemComponent;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface IItemRepository extends JpaRepository<Item, UUID> {
}
