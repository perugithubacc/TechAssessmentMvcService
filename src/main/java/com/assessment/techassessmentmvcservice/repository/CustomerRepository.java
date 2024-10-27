package com.assessment.techassessmentmvcservice.repository;

import com.assessment.techassessmentmvcservice.entity.CustomerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, Long>,
        JpaSpecificationExecutor<CustomerEntity> {

    @Query(value = "SELECT cust.* FROM customer cust " +
            "WHERE DATE_PART('year', AGE(CURRENT_DATE, cust.birthday)) > :ageGreaterThan",
            countQuery = "SELECT COUNT(*) FROM customer cust " +
                    "WHERE DATE_PART('year', AGE(CURRENT_DATE, cust.birthday)) > :ageGreaterThan",
            nativeQuery = true)
    Page<CustomerEntity> findByAgeGreaterThan(@Param("ageGreaterThan") Integer ageGreaterThan, Pageable pageable);
}
