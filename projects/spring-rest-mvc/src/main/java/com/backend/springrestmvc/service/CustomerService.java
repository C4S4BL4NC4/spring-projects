package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.Customer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface CustomerService {
    List<Customer> getAllCustomers();

    Customer getCustomerById(UUID id);
}
