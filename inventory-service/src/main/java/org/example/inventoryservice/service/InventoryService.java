package org.example.inventoryservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.ProductPurchaseEvent;
import org.example.inventoryservice.dto.InventoryRequest;
import org.example.inventoryservice.dto.InventoryResponse;
import org.example.inventoryservice.entity.Inventory;
import org.example.inventoryservice.repository.InventoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<InventoryResponse> isInStock(List<String> skuCode) {
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCode);
        return inventories.stream()
                .map(inventory ->
                        InventoryResponse.
                                builder()
                                .skuCode(inventory.getSkuCode())
                                .available(inventory.getQuantity() > 0)
                                .build()
                ).toList();

    }

    public void createInventory(InventoryRequest inventoryRequest) {
        Inventory inventory = Inventory.builder()
                .skuCode(inventoryRequest.getSkuCode())
                .quantity(inventoryRequest.getQuantity())
                .build();
        inventoryRepository.save(inventory);
    }

    @Transactional
    public void handlePurchaseEvent(List<ProductPurchaseEvent> productPurchaseEvents) {
        List<String> skuCodes = productPurchaseEvents.stream()
                .map(ProductPurchaseEvent::getSkuCode).toList();
        List<Inventory> inventories = inventoryRepository.findBySkuCodeIn(skuCodes);
        List<Inventory> inventoryList = new ArrayList<>();
        productPurchaseEvents.forEach(purchaseEvent -> {
            Inventory inventory = inventories.stream().filter(inv -> inv.getSkuCode().equals(purchaseEvent.getSkuCode())).findFirst()
                    .orElseGet(() -> new Inventory(null, purchaseEvent.getSkuCode(), purchaseEvent.getQuantity(), purchaseEvent.getProductId()));
            inventory.setQuantity(inventory.getQuantity() + purchaseEvent.getQuantity());
            inventoryList.add(inventory);
        });
        inventoryRepository.saveAll(inventoryList);
    }

    public List<Inventory> getAllInventory() {
        return inventoryRepository.findAll();
    }

    public ServerResponse getInventoryById(ServerRequest serverRequest) {
        Long id = Long.parseLong(serverRequest.pathVariable("id"));
        InventoryResponse inventoryResponse = inventoryRepository.findById(id)
                .map(inventory -> InventoryResponse.builder()
                        .skuCode(inventory.getSkuCode())
                        .available(inventory.getQuantity() > 0)
                        .build())
                .orElse(null);
        return ServerResponse.ok().body(inventoryResponse);
    }
}
