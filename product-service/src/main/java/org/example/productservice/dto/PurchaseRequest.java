package org.example.productservice.dto;

public record PurchaseRequest(
        String productId,
        Integer quantity,
        Double unitPrice,
        String skuCode
) {}
