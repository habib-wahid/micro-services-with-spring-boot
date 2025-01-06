package org.example.inventoryservice.service;

import org.example.ProductPurchaseEvent;
import org.example.inventoryservice.entity.InventoryEventSource;
import org.example.inventoryservice.repository.InventoryEventSourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class InventoryEventSourceService {

    private final InventoryEventSourceRepository inventoryEventSourceRepository;
    private final InventoryService inventoryService;
    public InventoryEventSourceService(InventoryEventSourceRepository inventoryEventSourceRepository, InventoryService inventoryService) {
        this.inventoryEventSourceRepository = inventoryEventSourceRepository;
        this.inventoryService = inventoryService;
    }

    @Transactional
    public void handlePurchaseEvent(List<ProductPurchaseEvent> purchaseEventList) {
        inventoryService.handlePurchaseEvent(purchaseEventList);
        List<InventoryEventSource> inventoryEventSourceList = purchaseEventList.stream()
                .map(purchaseEvent -> InventoryEventSource.builder()
                        .productId(purchaseEvent.getProductId())
                        .transactionQuantity(purchaseEvent.getQuantity())
                        .skuCode(purchaseEvent.getSkuCode())
                        .event(purchaseEvent.getEvent())
                        .build()).toList();
        inventoryEventSourceRepository.saveAll(inventoryEventSourceList);
    }
}
