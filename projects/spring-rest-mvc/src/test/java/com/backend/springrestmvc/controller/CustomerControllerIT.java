package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.entity.Customer;
import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class CustomerControllerIT {

    @Autowired
    CustomerController customerController;

    @Autowired
    CustomerRepository customerRepository;
    private RepositoryMethodInvocationListener repositoryMethodInvocationListener;


    @Test
    void getAllCustomers() {
        var dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Test
    void getCustomerById() {
        Customer customer = customerRepository.findAll().get(0);
        var dto = customerController.getCustomerById(customer.getId());
        assertThat(dto.getId()).isNotNull();
    }

    @Test
    void getCustomerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.getCustomerById(UUID.randomUUID()));
    }

    @Rollback
    @Transactional
    @Test
    void emptyList() {
        customerRepository.deleteAll();
        var dtos = customerController.getAllCustomers();
        assertThat(dtos.size()).isEqualTo(0);

    }
}