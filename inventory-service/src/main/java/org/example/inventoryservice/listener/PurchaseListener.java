package org.example.inventoryservice.listener;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.ProductPurchaseEvent;
import org.example.inventoryservice.service.InventoryEventSourceService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PurchaseListener {

    private final InventoryEventSourceService inventoryEventSourceService;
    ObjectMapper objectMapper = new ObjectMapper();

    public PurchaseListener(InventoryEventSourceService inventoryEventSourceService) {
        this.inventoryEventSourceService = inventoryEventSourceService;
    }

    @KafkaListener(topics = "purchase-topic", groupId = "${spring.kafka.consumer.group-id}")
    public void consumePurchaseEvent(List<Object> purchaseEventList) {
        log.info("Purchase event received: {}", purchaseEventList.getClass().getName());
        if (!purchaseEventList.isEmpty()) {
            List<ProductPurchaseEvent> events = objectMapper.convertValue(purchaseEventList, new TypeReference<List<ProductPurchaseEvent>>() {});
            for (ProductPurchaseEvent productPurchaseEvent : events) {
                System.out.println(productPurchaseEvent.getProductId());
            }
//            List<ProductPurchaseEvent> events = purchaseEventList.stream()
//                    .map(map -> objectMapper.convertValue(map, ProductPurchaseEvent.class))
//                    .toList();
//            events.forEach(event -> {
//                System.out.println("Event class: " + event.getClass().getName());
//            });
            inventoryEventSourceService.handlePurchaseEvent(events);
        } else {
            throw new RuntimeException("Purchase event list is empty");
        }
    }
}
