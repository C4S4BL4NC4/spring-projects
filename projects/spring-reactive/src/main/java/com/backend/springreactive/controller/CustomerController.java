package com.backend.springreactive.controller;

import com.backend.springreactive.model.CustomerDTO;
import com.backend.springreactive.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class CustomerController {
    private static final String CUSTOMER_URL = "/api/v0/customers";
    private static final String CUSTOMER_URL_ID = CUSTOMER_URL + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMER_URL)
    public Flux<CustomerDTO> listCustomers() {
        return customerService.listCustomers();
    }

    @GetMapping(CUSTOMER_URL_ID)
    public Mono<CustomerDTO> getCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(CUSTOMER_URL)
    public Mono<CustomerDTO> saveCustomer(@Validated @RequestBody CustomerDTO customerDto) {
        return customerService.createCustomer(customerDto);
    }

    @PutMapping(CUSTOMER_URL_ID)
    public Mono<CustomerDTO> updateCustomer(@PathVariable Integer customerId, @Validated @RequestBody CustomerDTO customerDto) {
        return customerService.updateCustomer(customerId, customerDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PatchMapping(CUSTOMER_URL_ID)
    public Mono<CustomerDTO> patchCustomer(@PathVariable Integer customerId, @RequestBody CustomerDTO customerDto) {
        return customerService.patchCustomer(customerId, customerDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping(CUSTOMER_URL_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteCustomer(@PathVariable Integer customerId) {
        return customerService.getCustomerById(customerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(foundCustomer -> customerService.deleteCustomerById(foundCustomer.getId()));
    }
}
