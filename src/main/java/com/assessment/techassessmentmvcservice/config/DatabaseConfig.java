package com.assessment.techassessmentmvcservice.config;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableJpaRepositories(basePackages = "com.assessment.techassessmentmvcservice.repository")
@EntityScan(basePackages = "com.assessment.techassessmentmvcservice.entity")
@EnableRedisRepositories
@EnableJpaAuditing
public class DatabaseConfig {
    @Bean
    @Primary
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
