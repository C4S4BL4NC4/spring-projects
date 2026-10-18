package com.backend.springreactive.repository;

import com.backend.springreactive.config.DatabaseConfig;
import com.backend.springreactive.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveNewCustomer() {
        StepVerifier.create(customerRepository.save(getNewCustomer()))
                .assertNext(customer -> {
                    assertThat(customer.getId()).isNotNull();
                    assertThat(customer.getCustomerName()).isEqualTo("Test Customer");
                    assertThat(customer.getCreatedDate()).isNotNull();
                })
                .verifyComplete();
    }

    @Test
    void findSavedCustomerById() {
        StepVerifier.create(customerRepository.save(getNewCustomer())
                        .flatMap(saved -> customerRepository.findById(saved.getId())))
                .assertNext(customer -> assertThat(customer.getCustomerName()).isEqualTo("Test Customer"))
                .verifyComplete();
    }

    Customer getNewCustomer() {
        return Customer.builder()
                .customerName("Test Customer")
                .build();
    }
}
