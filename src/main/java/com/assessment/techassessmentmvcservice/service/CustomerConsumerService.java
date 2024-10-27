package com.assessment.techassessmentmvcservice.service;

import com.assessment.techassessmentmvcservice.entity.CustomerEntity;
import com.assessment.techassessmentmvcservice.model.CustomerRecord;
import com.assessment.techassessmentmvcservice.repository.CustomerRepository;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

@Service
public class CustomerConsumerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerConsumerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @KafkaListener(
            topics = "${spring.kafka.tech-assessment.customer-topic.topic-name}",
            groupId = "${spring.kafka.tech-assessment.customer-topic.group-id}",
            concurrency = "${spring.kafka.tech-assessment.customer-topic.concurrency}"
    )
    public void consumeCustomerRecord(
            @Header("message-handler-key") String messageKey,
            ConsumerRecord<String, CustomerRecord> record
    ) {
        log.info("Received customer message key {}", messageKey);
        try {
            Long customerId = customerRepository.save(CustomerEntity.toEntity(record.value())).getCustomerId();
            log.info("Customer info saved for customerId: {}", customerId);
        } catch (Exception e) {
            log.error("Error occurred while saving customer data: {}", e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
