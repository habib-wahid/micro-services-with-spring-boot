package org.example.productservice.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(value = "product_purchase")
@AllArgsConstructor @NoArgsConstructor
@Getter @Setter
@Builder
public class ProductPurchase {

    @Id
    private String id;
    private String productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;

}
