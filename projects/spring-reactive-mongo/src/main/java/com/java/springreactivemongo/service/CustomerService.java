package com.java.springreactivemongo.service;

import com.java.springreactivemongo.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> findById(String id);

    Mono<CustomerDTO> saveCustomer(CustomerDTO customerDto);

    Mono<CustomerDTO> updateCustomer(String id, CustomerDTO customerDto);

    Mono<CustomerDTO> patchCustomer(String id, CustomerDTO customerDto);

    Mono<Void> deleteCustomerById(String id);
}
