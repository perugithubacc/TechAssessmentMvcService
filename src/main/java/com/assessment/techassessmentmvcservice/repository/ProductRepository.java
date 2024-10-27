package com.assessment.techassessmentmvcservice.repository;

import com.assessment.techassessmentmvcservice.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, String>,
        JpaSpecificationExecutor<ProductEntity> {
}
