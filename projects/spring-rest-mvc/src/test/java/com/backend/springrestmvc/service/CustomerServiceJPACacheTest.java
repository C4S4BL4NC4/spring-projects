package com.backend.springrestmvc.service;

import com.backend.springrestmvc.entity.Customer;
import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class CustomerServiceJPACacheTest {

    @Autowired
    CustomerService customerService;

    @Autowired
    CacheManager cacheManager;

    @MockitoSpyBean
    CustomerRepository customerRepository;

    UUID customerId;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("customerCache").clear();
        cacheManager.getCache("customerListCache").clear();
        customerId = customerRepository.findAll().getFirst().getId();
        clearInvocations(customerRepository);
    }

    @Test
    void getCustomerByIdIsCached() {
        var first = customerService.getCustomerById(customerId);
        var second = customerService.getCustomerById(customerId);

        assertThat(second).isEqualTo(first);
        verify(customerRepository, times(1)).findById(customerId);
    }

    @Test
    void getAllCustomersIsCached() {
        customerService.getAllCustomers();
        customerService.getAllCustomers();

        verify(customerRepository, times(1)).findAll();
    }

    @Rollback
    @Transactional
    @Test
    void createCustomerClearsListCache() {
        var before = customerService.getAllCustomers();

        customerService.createCustomer(CustomerDTO.builder().name("Cache Test Customer").build());

        assertThat(customerService.getAllCustomers()).hasSize(before.size() + 1);
        verify(customerRepository, times(2)).findAll();
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomerEvictsCustomerAndClearsListCache() {
        customerService.getCustomerById(customerId);
        customerService.getAllCustomers();

        customerService.updateCustomer(customerId, CustomerDTO.builder().name("Updated Name").build());

        assertThat(cacheManager.getCache("customerCache").get(customerId)).isNull();
        assertThat(customerService.getCustomerById(customerId).orElseThrow().getName()).isEqualTo("Updated Name");

        customerService.getAllCustomers();
        verify(customerRepository, times(2)).findAll();
    }

    @Rollback
    @Transactional
    @Test
    void deleteCustomerEvictsCustomerAndClearsListCache() {
        // bootstrapped customers have orders, so delete one without orders
        UUID customerId = customerRepository.save(Customer.builder()
                .name("Delete Me")
                .build()).getId();
        clearInvocations(customerRepository);

        customerService.getCustomerById(customerId);
        var before = customerService.getAllCustomers();

        assertThat(customerService.deleteCustomer(customerId)).isTrue();

        assertThat(cacheManager.getCache("customerCache").get(customerId)).isNull();
        assertThat(customerService.getCustomerById(customerId)).isEmpty();
        assertThat(customerService.getAllCustomers()).hasSize(before.size() - 1);
    }

    @Test
    void deleteMissingCustomerKeepsCaches() {
        customerService.getCustomerById(customerId);
        customerService.getAllCustomers();

        assertThat(customerService.deleteCustomer(UUID.randomUUID())).isFalse();

        customerService.getCustomerById(customerId);
        customerService.getAllCustomers();
        verify(customerRepository, times(1)).findById(customerId);
        verify(customerRepository, times(1)).findAll();
    }
}
