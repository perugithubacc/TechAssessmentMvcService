package com.assessment.techassessmentmvcservice.controller;

import com.assessment.techassessmentmvcservice.service.ProductService;
import com.techassessment.techassessmentmvcservice.api.ProductsApi;
import com.techassessment.techassessmentmvcservice.model.Product;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/techassessmentmvcservice/api/v1/")
public class ProductApiController implements ProductsApi {
    private final ProductService productService;

    @Autowired
    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public ResponseEntity<Product> createProduct(Product product) {
       return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(product));
    }

    @Override
    public ResponseEntity<Void> deleteProductByProductCode(String productCode) {
        productService.deleteProduct(productCode);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Product> getProductByProductCode(String productCode) {
        return ResponseEntity.ok(productService.getProductByProductCode(productCode));
    }

    @Override
    public ResponseEntity<List<Product>> listProducts(Integer page, Integer size) {
        return ResponseEntity.ok(productService.listProducts(page, size));
    }

    @Override
    public ResponseEntity<List<Product>> searchProducts(String field, String value) {
        return ResponseEntity.ok(productService.searchProducts(field, value));
    }

    @Override
    public ResponseEntity<Product> updateProductByProductCode(String productCode, Product product) {
        return ResponseEntity.ok(productService.updateProduct(productCode, product));
    }
}
