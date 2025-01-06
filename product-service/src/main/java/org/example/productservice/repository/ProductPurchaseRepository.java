package org.example.productservice.repository;

import org.example.productservice.entity.ProductPurchase;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductPurchaseRepository extends MongoRepository<ProductPurchase, String> {
}
