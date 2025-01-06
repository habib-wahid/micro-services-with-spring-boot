package org.example.productservice.service;

import org.example.ProductPurchaseEvent;
import org.example.enums.EventEnum;
import org.example.productservice.dto.PurchaseRequest;
import org.example.productservice.entity.Product;
import org.example.productservice.entity.ProductPurchase;
import org.example.productservice.repository.ProductPurchaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProductPurchaseService {

    private final ProductPurchaseRepository productPurchaseRepository;
    private final ProductService productService;
    private final KafkaTemplate<String, List<ProductPurchaseEvent>> kafkaTemplate;

    @Autowired
    public ProductPurchaseService(
            ProductPurchaseRepository productPurchaseRepository,
            ProductService productService,
            KafkaTemplate<String, List<ProductPurchaseEvent>> kafkaTemplate) {
        this.productPurchaseRepository = productPurchaseRepository;
        this.productService = productService;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Transactional
    public void purchase(List<PurchaseRequest> purchaseRequest) {
        List<String> productIds = purchaseRequest.stream()
                .map(PurchaseRequest::productId)
                .toList();
        System.out.println("Purchase request " + productIds);
        if (productService.checkAllProductsExist(productIds)) {
            List<ProductPurchase> productPurchases = purchaseRequest.stream()
                    .map(purchase -> {
                        ProductPurchase productPurchase = new ProductPurchase();
                        productPurchase.setProductId(purchase.productId());
                        productPurchase.setQuantity(purchase.quantity());
                        productPurchase.setUnitPrice(purchase.unitPrice());
                        productPurchase.setTotalPrice(purchase.unitPrice() * purchase.quantity());
                        return productPurchase;
                    }).toList();
            productPurchaseRepository.saveAll(productPurchases);

            // Creating purchase event

            List<ProductPurchaseEvent> purchaseEvents = purchaseRequest.stream()
                    .map(purchase -> ProductPurchaseEvent.builder()
                            .productId(purchase.productId())
                            .quantity(purchase.quantity())
                            .skuCode(purchase.skuCode())
                            .event(EventEnum.PRODUCT_PURCHASE.getValue())
                            .build()).toList();
            kafkaTemplate.send("purchase-topic", purchaseEvents);
        } else {
            throw new RuntimeException("Product not found");
        }
    }

}
