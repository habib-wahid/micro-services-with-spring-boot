package org.example.inventoryservice.repository;

import org.example.inventoryservice.entity.InventoryEventSource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryEventSourceRepository extends JpaRepository<InventoryEventSource, Long> {
}
