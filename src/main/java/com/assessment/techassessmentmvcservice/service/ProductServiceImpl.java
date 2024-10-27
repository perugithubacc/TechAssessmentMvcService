package com.assessment.techassessmentmvcservice.service;

import com.assessment.techassessmentmvcservice.Exceptions.ProductNotFoundException;
import com.assessment.techassessmentmvcservice.Exceptions.NoChangeFoundException;
import com.assessment.techassessmentmvcservice.entity.ProductEntity;
import com.assessment.techassessmentmvcservice.repository.ProductRepository;
import com.assessment.techassessmentmvcservice.spec.DynamicSearchSpecifications;
import com.techassessment.techassessmentmvcservice.model.Product;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService{

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);
    private final ProductRepository productRepository;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public Product createProduct(Product product) {
        try {
            product.setProductId(UUID.randomUUID().toString());
            productRepository.save(new ProductEntity().toEntity(product));
        } catch (DataIntegrityViolationException ex) {
            log.error("Error saving product: {}", ex.getMessage());
            throw new DataIntegrityViolationException(ex.getMessage());
        }
        log.info("Product info saved for productId: {}", product.getProductId());
        return product;
    }

    @CachePut(value = "products", key = "#product.productId")
    @Override
    public Product updateProduct(String productId, Product product) {
        log.info("Updating product info for productId: {}", productId);
        ProductEntity existingProductInfo = productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product Info not found for productId: " +
                                product.getProductId())
                );
        ProductEntity newProductInfo = new ProductEntity().toEntity(product);
        if (!existingProductInfo.equals(newProductInfo)) {
            return productRepository.save(newProductInfo).toDto();
        } else {
            log.error("No changes are found for productId: {}", productId);
            throw new NoChangeFoundException("No changes are found for productId: " + productId);
        }
    }

    @CacheEvict(value = "products", allEntries = true)
    @Override
    public void deleteProduct(String productId) {
        if (!productRepository.existsById(productId)) {
            log.error("Product Info not found for productId: " + productId);
            throw new ProductNotFoundException("Product Info not found for productId: " + productId);
        }
        productRepository.deleteById(productId);

        log.info("Product Info deleted successfully for productId:{}", productId);
    }

    @Cacheable(value = "products", key = "#productId")
    @Override
    public Product getProductByProductCode(String productId) {
        return productRepository.findById(productId)
                .orElseThrow(() ->
                        new ProductNotFoundException("Product Info not found for productId: " + productId)
                ).toDto();
    }

    @Cacheable(value = "products", key = "#page + '-' + #size")
    @Override
    public List<Product> listProducts(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return productRepository.findAll(pageable).map(ProductEntity::toDto).getContent();
    }

    @Cacheable(value = "products", key = "#field + '-' + #value")
    @Override
    public List<Product> searchProducts(String field, String value) {
        Specification<ProductEntity> spec = DynamicSearchSpecifications.hasField(ProductEntity.class, field, value);

        return productRepository.findAll(spec).stream()
                .map(ProductEntity::toDto)
                .toList();
    }
}
