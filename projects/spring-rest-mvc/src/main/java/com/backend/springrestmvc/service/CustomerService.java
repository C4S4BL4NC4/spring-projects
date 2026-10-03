package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.CustomerDTO;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface CustomerService {
    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    void updateCustomer(UUID id, CustomerDTO customerDTO);

    void deleteCustomer(UUID customerId);
}
