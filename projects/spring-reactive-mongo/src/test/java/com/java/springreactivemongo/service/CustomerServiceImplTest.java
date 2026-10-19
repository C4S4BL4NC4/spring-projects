package com.java.springreactivemongo.service;

import com.java.springreactivemongo.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class CustomerServiceImplTest {

    @Autowired
    CustomerService customerService;

    @Test
    void saveCustomer() {
        AtomicReference<CustomerDTO> savedCustomer = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .subscribe(savedCustomer::set);

        await().until(() -> savedCustomer.get() != null);

        assertThat(savedCustomer.get().getId()).isNotNull();
        assertThat(savedCustomer.get().getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void listCustomers() {
        AtomicReference<List<CustomerDTO>> foundCustomers = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .thenMany(customerService.listCustomers())
                .collectList()
                .subscribe(foundCustomers::set);

        await().until(() -> foundCustomers.get() != null);

        assertThat(foundCustomers.get()).isNotEmpty();
    }

    @Test
    void findById() {
        AtomicReference<CustomerDTO> foundCustomer = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .flatMap(saved -> customerService.findById(saved.getId()))
                .subscribe(foundCustomer::set);

        await().until(() -> foundCustomer.get() != null);

        assertThat(foundCustomer.get().getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void updateCustomer() {
        AtomicReference<CustomerDTO> updatedCustomer = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .flatMap(saved -> customerService.updateCustomer(saved.getId(),
                        CustomerDTO.builder().customerName("Updated Name").build()))
                .subscribe(updatedCustomer::set);

        await().until(() -> updatedCustomer.get() != null);

        assertThat(updatedCustomer.get().getCustomerName()).isEqualTo("Updated Name");
    }

    @Test
    void patchCustomer() {
        AtomicReference<CustomerDTO> patchedCustomer = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .flatMap(saved -> customerService.patchCustomer(saved.getId(),
                        CustomerDTO.builder().customerName("Patched Name").build()))
                .subscribe(patchedCustomer::set);

        await().until(() -> patchedCustomer.get() != null);

        assertThat(patchedCustomer.get().getCustomerName()).isEqualTo("Patched Name");
    }

    @Test
    void patchCustomerNullNameKeepsExisting() {
        AtomicReference<CustomerDTO> patchedCustomer = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .flatMap(saved -> customerService.patchCustomer(saved.getId(), new CustomerDTO()))
                .subscribe(patchedCustomer::set);

        await().until(() -> patchedCustomer.get() != null);

        assertThat(patchedCustomer.get().getCustomerName()).isEqualTo("John Doe");
    }

    @Test
    void deleteCustomerById() {
        AtomicBoolean completed = new AtomicBoolean(false);
        AtomicReference<CustomerDTO> foundCustomer = new AtomicReference<>();

        customerService.saveCustomer(getNewCustomerDto())
                .flatMap(saved -> customerService.deleteCustomerById(saved.getId())
                        .then(customerService.findById(saved.getId())))
                .subscribe(foundCustomer::set, null, () -> completed.set(true));

        await().untilTrue(completed);

        assertThat(foundCustomer.get()).isNull();
    }

    CustomerDTO getNewCustomerDto() {
        return CustomerDTO.builder()
                .customerName("John Doe")
                .build();
    }
}
