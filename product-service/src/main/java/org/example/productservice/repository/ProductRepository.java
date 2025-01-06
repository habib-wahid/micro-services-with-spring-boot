package org.example.productservice.repository;

import org.example.productservice.entity.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ProductRepository extends MongoRepository<Product, String> {

    @Query(value = "{ '_id' : { '$in' :  ?0} }", count = true)
    long countByIds(List<String> productIds);
}
