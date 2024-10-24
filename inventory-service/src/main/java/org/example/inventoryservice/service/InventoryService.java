package org.example.inventoryservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.inventoryservice.dto.InventoryRequest;
import org.example.inventoryservice.entity.Inventory;
import org.example.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean isInStock(String skuCode) {
        return inventoryRepository.findBySkuCode(skuCode).isPresent();
    }

    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();
        inventoryRepository.save(inventory);
    }
}
