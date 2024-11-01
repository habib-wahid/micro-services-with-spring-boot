package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.InventoryResponse;
import org.example.orderservice.dto.OrderItemDto;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItems;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        List<String> skuCodes = orderRequest.getOrderItemDtoList().stream()
                .map(OrderItemDto::getSkuCode)
                .toList();

        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://localhost:8082/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        if (inventoryResponses == null || inventoryResponses.length == 0) {
            throw new IllegalArgumentException("None of the items are available. please try again");
        }
        Arrays.stream(inventoryResponses).forEach(inventoryResponse -> {
            System.out.println(inventoryResponse.getSkuCode() + " " + inventoryResponse.isAvailable());
        });

        boolean productsInStock = Arrays.stream(inventoryResponses)
                .allMatch(InventoryResponse::isAvailable);

        if (productsInStock) {
            Order order = new Order();
            order.setOrderNumber(UUID.randomUUID().toString());
            List<OrderItems> itemsList =  orderRequest.getOrderItemDtoList().stream()
                    .map(this::mapToOrderItem)
                    .toList();
            order.setOrderItems(itemsList);
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product not in stock, try again later");
        }

    }

    private OrderItems mapToOrderItem(OrderItemDto orderItemDto) {
        return OrderItems.builder()
                .skuCode(orderItemDto.getSkuCode())
                .quantity(orderItemDto.getQuantity())
                .price(orderItemDto.getPrice())
                .build();
    }
}
