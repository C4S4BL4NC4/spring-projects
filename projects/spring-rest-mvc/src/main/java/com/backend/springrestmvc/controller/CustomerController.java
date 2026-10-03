package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
public class CustomerController {

    public static final String CUSTOMERS_PATH = "/api/v0/customers";
    public static final String CUSTOMERS_PATH_ID = CUSTOMERS_PATH + '/' + "{customerId}";


    private final CustomerService customerService;

    @DeleteMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<CustomerDTO> deleteBeer(@PathVariable("customerId") UUID customerId) {
        log.debug("Deleting customer with id {}", customerId);
        customerService.deleteCustomer(customerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(CUSTOMERS_PATH_ID)
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable UUID customerId, @RequestBody CustomerDTO customerDTO) {
        customerService.updateCustomer(customerId, customerDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping(CUSTOMERS_PATH)
    public List<CustomerDTO> getAllCustomers() {
        log.debug("getAllCustomers() - in CustomerController");
        return customerService.getAllCustomers();
    }

    @GetMapping(CUSTOMERS_PATH_ID)
    public CustomerDTO getCustomerById(@PathVariable("customerId") UUID customerId) {
        log.debug("getCustomerById() - in CustomerController");
        return customerService.getCustomerById(customerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(CUSTOMERS_PATH)
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO customerDTO) {
        log.debug("createCustomer() - in CustomerController");
        var savedCustomer = customerService.createCustomer(customerDTO);
        return new ResponseEntity<>(savedCustomer, HttpStatus.CREATED);
    }
}
