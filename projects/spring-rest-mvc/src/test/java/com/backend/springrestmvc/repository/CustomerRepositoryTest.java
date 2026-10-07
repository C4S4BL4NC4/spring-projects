package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CustomerRepositoryTest {

    Customer customer;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customer = Customer.builder().name("Alex Boris").build();
    }

    @Test
    void testSaveCustomer() {
        var savedCustomer = customerRepository.save(customer);
        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
    }
}