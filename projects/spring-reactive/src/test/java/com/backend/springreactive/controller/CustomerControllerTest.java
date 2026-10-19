package com.backend.springreactive.controller;

import com.backend.springreactive.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerControllerTest {

    private static final String CUSTOMER_URL = "/api/v2/customers";
    private static final String CUSTOMER_URL_ID = CUSTOMER_URL + "/{customerId}";

    @Autowired
    WebTestClient webTestClient;

    @Test
    void listCustomers() {
        createCustomer();

        webTestClient.get().uri(CUSTOMER_URL)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerDTO.class)
                .value(customers -> assertThat(customers).isNotEmpty());
    }

    @Test
    void getCustomer() {
        CustomerDTO savedCustomer = createCustomer();

        webTestClient.get().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> assertThat(customer.getCustomerName()).isEqualTo("Test Customer"));
    }

    @Test
    void getCustomerNotFound() {
        webTestClient.get().uri(CUSTOMER_URL_ID, 999999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createCustomerBadData() {
        webTestClient.post().uri(CUSTOMER_URL)
                .bodyValue(CustomerDTO.builder().customerName("").build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomer() {
        CustomerDTO savedCustomer = createCustomer();

        webTestClient.put().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .bodyValue(CustomerDTO.builder().customerName("Updated Name").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> {
                    assertThat(customer.getId()).isEqualTo(savedCustomer.getId());
                    assertThat(customer.getCustomerName()).isEqualTo("Updated Name");
                });

        webTestClient.get().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> assertThat(customer.getCustomerName()).isEqualTo("Updated Name"));
    }

    @Test
    void updateCustomerBadData() {
        CustomerDTO savedCustomer = createCustomer();

        webTestClient.put().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .bodyValue(CustomerDTO.builder().customerName("ab").build())
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomerNotFound() {
        webTestClient.put().uri(CUSTOMER_URL_ID, 999999)
                .bodyValue(CustomerDTO.builder().customerName("Updated Name").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchCustomer() {
        CustomerDTO savedCustomer = createCustomer();

        webTestClient.patch().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .bodyValue(CustomerDTO.builder().customerName("Patched Name").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> assertThat(customer.getCustomerName()).isEqualTo("Patched Name"));
    }

    @Test
    void patchCustomerNotFound() {
        webTestClient.patch().uri(CUSTOMER_URL_ID, 999999)
                .bodyValue(CustomerDTO.builder().customerName("Patched Name").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteCustomer() {
        CustomerDTO savedCustomer = createCustomer();

        webTestClient.delete().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(CUSTOMER_URL_ID, savedCustomer.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteCustomerNotFound() {
        webTestClient.delete().uri(CUSTOMER_URL_ID, 999999)
                .exchange()
                .expectStatus().isNotFound();
    }

    CustomerDTO createCustomer() {
        CustomerDTO savedCustomer = webTestClient.post().uri(CUSTOMER_URL)
                .bodyValue(CustomerDTO.builder().customerName("Test Customer").build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(savedCustomer).isNotNull();
        assertThat(savedCustomer.getId()).isNotNull();
        return savedCustomer;
    }
}
