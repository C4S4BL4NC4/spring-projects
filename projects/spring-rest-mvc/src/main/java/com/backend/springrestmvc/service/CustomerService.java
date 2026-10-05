package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.CustomerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerService {

    List<CustomerDTO> getAllCustomers();

    Optional<CustomerDTO> getCustomerById(UUID id);

    CustomerDTO createCustomer(CustomerDTO customerDTO);

    void updateCustomer(UUID id, CustomerDTO customerDTO);

    void deleteCustomer(UUID id);
}
