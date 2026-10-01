package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.Customer;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CustomerService {
    List<Customer> getAllCustomers();

    Customer getCustomerById(UUID id);

    Customer createCustomer(Customer customer);

    void updateCustomer(UUID id, Customer customer);

    void deleteCustomer(UUID customerId);
}
