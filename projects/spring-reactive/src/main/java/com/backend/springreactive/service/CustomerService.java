package com.backend.springreactive.service;

import com.backend.springreactive.model.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerService {

    Flux<CustomerDTO> listCustomers();

    Mono<CustomerDTO> getCustomerById(Integer customerId);

    Mono<CustomerDTO> createCustomer(CustomerDTO customerDto);

    Mono<CustomerDTO> updateCustomer(Integer customerId, CustomerDTO customerDto);

    Mono<CustomerDTO> patchCustomer(Integer customerId, CustomerDTO customerDto);

    Mono<Void> deleteCustomerById(Integer customerId);
}
