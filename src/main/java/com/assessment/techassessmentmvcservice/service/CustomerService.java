package com.assessment.techassessmentmvcservice.service;

import com.techassessment.techassessmentmvcservice.model.Customer;
import java.util.List;

public interface CustomerService {
    Customer saveCustomer(Customer customer);
    Customer updateCustomer(String customerId, Customer customer);
    void removeCustomer(String customerId);

    Customer getCustomerById(String customerId);
    List<Customer> getCustomers(
            Integer page,
            Integer size,
            Integer ageGreaterThan
    );
    List<Customer> searchCustomers(String field, String value);
}
