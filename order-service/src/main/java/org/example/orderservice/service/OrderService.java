package org.example.orderservice.service;

import lombok.RequiredArgsConstructor;
import org.example.orderservice.dto.OrderItemDto;
import org.example.orderservice.dto.OrderRequest;
import org.example.orderservice.entity.Order;
import org.example.orderservice.entity.OrderItems;
import org.example.orderservice.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        List<OrderItems> itemsList =  orderRequest.getOrderItemDtoList().stream()
                .map(this::mapToOrderItem)
                .toList();

        order.setOrderItems(itemsList);
        orderRepository.save(order);
    }

    private OrderItems mapToOrderItem(OrderItemDto orderItemDto) {
        return OrderItems.builder()
                .skuCode(orderItemDto.getSkuCode())
                .quantity(orderItemDto.getQuantity())
                .price(orderItemDto.getPrice())
                .build();
    }
}
