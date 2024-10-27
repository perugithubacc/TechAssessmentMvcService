package com.assessment.techassessmentmvcservice.controller;

import com.assessment.techassessmentmvcservice.service.CustomerProducerService;
import com.assessment.techassessmentmvcservice.service.CustomerService;
import com.techassessment.techassessmentmvcservice.api.CustomersApi;
import com.techassessment.techassessmentmvcservice.model.Customer;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/techassessmentservice/api/v1/")
public class CustomerApiController implements CustomersApi {

    private final CustomerService customerService;
    private final CustomerProducerService customerProducerService;

    public CustomerApiController(CustomerService customerService, CustomerProducerService customerProducerService) {
        this.customerService = customerService;
        this.customerProducerService = customerProducerService;
    }

    @Override
    public ResponseEntity<Customer> createCustomer(Customer customer) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(customerService.saveCustomer(customer));
    }

    @Override
    public ResponseEntity<Void> deleteCustomer(String customerId) {
        customerService.removeCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Customer> getCustomerById(String customerId) {
        return ResponseEntity.ok(customerService.getCustomerById(customerId));
    }

    @Override
    public ResponseEntity<Customer> updateCustomer(String customerId, Customer customer) {
        return ResponseEntity.ok(customerService.updateCustomer(customerId, customer));
    }

    @Override
    public ResponseEntity<List<Customer>> getCustomers(Integer page, Integer size, Integer ageGreaterThan) {
        return ResponseEntity.ok(customerService.getCustomers(page, size, ageGreaterThan));
    }

    @Override
    public ResponseEntity<List<Customer>> searchCustomers(String field, String value) {
        return ResponseEntity.ok(customerService.searchCustomers(field, value));
    }

    @Override
    public ResponseEntity<String> submitNewCustomer(Customer customer) {
        customerProducerService.publish(customer);
        return ResponseEntity.ok("Customer creation submitted");
    }
}
