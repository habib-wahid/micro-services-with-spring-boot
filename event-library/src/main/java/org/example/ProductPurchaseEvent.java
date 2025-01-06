package org.example;

import lombok.*;

@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ProductPurchaseEvent {
    private String productId;
    private int quantity;
    private String skuCode;
    private String event;

}
