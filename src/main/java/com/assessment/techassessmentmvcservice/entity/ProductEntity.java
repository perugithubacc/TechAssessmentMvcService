package com.assessment.techassessmentmvcservice.entity;

import com.techassessment.techassessmentmvcservice.model.Product;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@ToString
@EqualsAndHashCode
public class ProductEntity {
    @Id
    private String productId;

    @Column(name = "product_code", nullable = false, unique = true)
    private String productCode;

    @Column(name="product_name", nullable = false)
    private String productName;

    @Column(name = "product_description")
    private String productDescription;

    @Column(name="product_price", nullable = false)
    private BigDecimal productPrice;

    @Column(name="product_quantity", nullable = false)
    private Integer productQuantity;

    @Column(name="product_type", nullable = false)
    private String productType;

    public Product toDto() {
        return Product.builder()
                .productCode(this.productCode)
                .productDescription(this.productDescription)
                .productId(this.productId)
                .productName(this.productName)
                .productPrice(this.productPrice)
                .productQuantity(this.productQuantity)
                .productType(this.productType)
                .build();
    }

    public ProductEntity toEntity(Product product) {
        return ProductEntity.builder()
                .productId(product.getProductId())
                .productCode(product.getProductCode())
                .productDescription(product.getProductDescription())
                .productName(product.getProductName())
                .productPrice(product.getProductPrice())
                .productQuantity(product.getProductQuantity())
                .productType(product.getProductType())
                .build();
    }
}
