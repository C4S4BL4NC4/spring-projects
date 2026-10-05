package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CustomerController {
    public static final String CUSTOMERS_PATH = "/api/v0/customers";
    public static final String CUSTOMERS_PATH_ID = CUSTOMERS_PATH + "/{customerId}";

    private final CustomerService customerService;

    @GetMapping(CUSTOMERS_PATH)
    public List<CustomerDTO> getAllCustomers() {
        log.debug("getAllCustomers() - in CustomerController");
        return customerService.getAllCustomers();
    }

    @GetMapping(CUSTOMERS_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable UUID customerId) {
        log.debug("getCustomerById() - in CustomerController, customerId = {}", customerId);
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMERS_PATH)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.debug("createCustomer() - in CustomerController");
        CustomerDTO savedCustomerDTO = customerService.createCustomer(customerDTO);
        return ResponseEntity
                .created(URI.create(CUSTOMERS_PATH + "/" + savedCustomerDTO.getId()))
                .body(savedCustomerDTO);
    }

    @PutMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<Void> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customerDTO) {
        log.debug("updateCustomer() - in CustomerController, customerId = {}", customerId);
        customerService.updateCustomer(customerId, customerDTO);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<Void> deleteCustomer(@PathVariable UUID customerId) {
        log.debug("deleteCustomer() - in CustomerController, customerId = {}", customerId);
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }
}
