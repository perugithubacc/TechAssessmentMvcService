package com.assessment.techassessmentmvcservice.service;

import com.assessment.techassessmentmvcservice.Exceptions.NoChangeFoundException;
import com.assessment.techassessmentmvcservice.Exceptions.CustomerNotFoundException;
import com.assessment.techassessmentmvcservice.entity.CustomerEntity;
import com.assessment.techassessmentmvcservice.repository.CustomerRepository;
import com.assessment.techassessmentmvcservice.spec.DynamicSearchSpecifications;
import com.techassessment.techassessmentmvcservice.model.Customer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;


@Service
public class CustomerServiceImpl implements CustomerService {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceImpl.class);

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(
            CustomerRepository customerRepository
    ) {
        this.customerRepository = customerRepository;
    }

    @CacheEvict(value = "customers", allEntries = true)
    @Override
    public Customer saveCustomer(Customer customer) {
        CustomerEntity customerEntity;
        try {
            customerEntity = customerRepository.save(new CustomerEntity().toEntity(customer));
            customer.setCustomerId(customerEntity.getCustomerId().toString());
        } catch (DataIntegrityViolationException ex) {
            log.error("Error saving customer: {}", ex.getMessage());
            throw new DataIntegrityViolationException(ex.getMessage());
        }
        log.info("Customer info saved for customerId: {}", customerEntity.getCustomerId());
        return customer;
    }

    @CachePut(value = "customers", key = "#customer.customerId")
    @Override
    public Customer updateCustomer(String customerId, Customer customer) {
        log.info("Updating customer info for customerId: {}", customerId);
        CustomerEntity existingCustomerInfo = customerRepository.findById(Long.valueOf(customerId))
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer Info not found for customerId: " +
                                customer.getCustomerId())
                );
        CustomerEntity newCustomerInfo = new CustomerEntity().toEntity(customer);
        if (!existingCustomerInfo.equals(newCustomerInfo)) {
            return customerRepository.save(newCustomerInfo).toDto();
        } else {
            log.error("No changes are found for customerId: {}", customerId);
            throw new NoChangeFoundException("No changes are found for customerId: " + customerId);
        }
    }

    @CacheEvict(value = "customers", allEntries = true)
    @Override
    public void removeCustomer(String customerId) {
        if (!customerRepository.existsById(Long.valueOf(customerId))) {
            log.error("Customer Info not found for customerId: " + customerId);
            throw new CustomerNotFoundException("Customer Info not found for customerId: " + customerId);
        };

        customerRepository.deleteById(Long.valueOf(customerId));

        log.info("Customer Info deleted successfully for customerId:{}", customerId);
    }

    @Cacheable(value = "customers", key = "#customerId")
    @Override
    public Customer getCustomerById(String customerId) {
        return customerRepository.findById(Long.valueOf(customerId))
                .orElseThrow(() ->
                        new CustomerNotFoundException("Customer Info not found for customerId: " + customerId))
                .toDto();
    }

    @Cacheable(value = "customers", key = "#page + '-' + #size + '-' + #ageGreaterThan")
    @Override
    public List<Customer> getCustomers(Integer page, Integer size, Integer ageGreaterThan) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CustomerEntity> customerEntity;
        if (ageGreaterThan != null && ageGreaterThan > 0) {
            customerEntity = customerRepository.findByAgeGreaterThan(ageGreaterThan, pageable);
        } else {
            customerEntity = customerRepository.findAll(pageable);
        }
        return customerEntity.map(CustomerEntity::toDto).getContent();
    }

    @Cacheable(value = "customers", key = "#field + '-' + #value")
    @Override
    public List<Customer> searchCustomers(String field, String value) {
        Specification<CustomerEntity> spec = DynamicSearchSpecifications.hasField(CustomerEntity.class,field, value);

        return customerRepository.findAll(spec).stream()
                .map(CustomerEntity::toDto)
                .toList();
    }
}
