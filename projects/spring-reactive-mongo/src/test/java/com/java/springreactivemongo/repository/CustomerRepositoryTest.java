package com.java.springreactivemongo.repository;

import com.java.springreactivemongo.config.MongoConfig;
import com.java.springreactivemongo.domain.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@DataMongoTest
@Import(MongoConfig.class)
class CustomerRepositoryTest {

    @Autowired
    CustomerRepository customerRepository;

    @Test
    void saveNewCustomer() {
        AtomicReference<Customer> savedCustomer = new AtomicReference<>();

        customerRepository.save(getNewCustomer())
                .subscribe(savedCustomer::set);

        await().until(() -> savedCustomer.get() != null);

        assertThat(savedCustomer.get().getId()).isNotNull();
        assertThat(savedCustomer.get().getCustomerName()).isEqualTo("Test Customer");
    }

    @Test
    void findSavedCustomerById() {
        AtomicReference<Customer> foundCustomer = new AtomicReference<>();

        customerRepository.save(getNewCustomer())
                .flatMap(saved -> customerRepository.findById(saved.getId()))
                .subscribe(foundCustomer::set);

        await().until(() -> foundCustomer.get() != null);

        assertThat(foundCustomer.get().getCustomerName()).isEqualTo("Test Customer");
    }

    @Test
    void deleteSavedCustomer() {
        AtomicBoolean completed = new AtomicBoolean(false);
        AtomicReference<Customer> foundCustomer = new AtomicReference<>();

        customerRepository.save(getNewCustomer())
                .flatMap(saved -> customerRepository.deleteById(saved.getId())
                        .then(customerRepository.findById(saved.getId())))
                .subscribe(foundCustomer::set, null, () -> completed.set(true));

        await().untilTrue(completed);

        assertThat(foundCustomer.get()).isNull();
    }

    Customer getNewCustomer() {
        return Customer.builder()
                .customerName("Test Customer")
                .build();
    }
}
