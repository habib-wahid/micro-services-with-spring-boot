package org.example.productservice.controller;

import org.example.productservice.dto.PurchaseRequest;
import org.example.productservice.service.ProductPurchaseService;
import org.example.productservice.service.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product/purchase")
public class PurchaseController {

    private final ProductPurchaseService productPurchaseService;

    public PurchaseController(ProductPurchaseService productPurchaseService) {
        this.productPurchaseService = productPurchaseService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void purchase(@RequestBody List<PurchaseRequest> purchaseRequest) {
        productPurchaseService.purchase(purchaseRequest);
    }
}
