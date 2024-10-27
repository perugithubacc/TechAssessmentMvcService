package com.assessment.techassessmentmvcservice.service;

import com.techassessment.techassessmentmvcservice.model.Product;
import java.util.List;

public interface ProductService {
    Product createProduct(Product product);
    Product updateProduct(String productId, Product product);
    void deleteProduct(String productId);

    Product getProductByProductCode(String productId);
    List<Product> listProducts(
            Integer page,
            Integer size
    );
    List<Product> searchProducts(String field, String value);
}
