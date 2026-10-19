package com.java.springreactivemongo.webfn;

import com.java.springreactivemongo.model.CustomerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void listCustomers() {
        createCustomer();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerDTO.class)
                .value(customers -> assertThat(customers).isNotEmpty());
    }

    @Test
    void getCustomerById() {
        String customerId = createCustomer();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> {
                    assertThat(customer.getId()).isEqualTo(customerId);
                    assertThat(customer.getCustomerName()).isEqualTo("John Doe");
                });
    }

    @Test
    void getCustomerNotFound() {
        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, "999999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createCustomer201() {
        webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .bodyValue(getNewCustomerDto())
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location");
    }

    @Test
    void createCustomerBadData() {
        CustomerDTO customer = getNewCustomerDto();
        customer.setCustomerName("");

        webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .bodyValue(customer)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomer() {
        String customerId = createCustomer();

        CustomerDTO update = getNewCustomerDto();
        update.setCustomerName("Updated Name");

        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .bodyValue(update)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> assertThat(customer.getCustomerName()).isEqualTo("Updated Name"));
    }

    @Test
    void updateCustomerBadData() {
        String customerId = createCustomer();

        CustomerDTO update = getNewCustomerDto();
        update.setCustomerName("ab");

        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateCustomerNotFound() {
        webTestClient.put().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, "999999")
                .bodyValue(getNewCustomerDto())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchCustomer() {
        String customerId = createCustomer();

        CustomerDTO patch = CustomerDTO.builder()
                .customerName("Patched Name")
                .build();

        webTestClient.patch().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .bodyValue(patch)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CustomerDTO.class)
                .value(customer -> assertThat(customer.getCustomerName()).isEqualTo("Patched Name"));
    }

    @Test
    void patchCustomerNotFound() {
        webTestClient.patch().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, "999999")
                .bodyValue(CustomerDTO.builder().customerName("Patched Name").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteCustomer() {
        String customerId = createCustomer();

        webTestClient.delete().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, customerId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteCustomerNotFound() {
        webTestClient.delete().uri(CustomerRouterConfig.CUSTOMER_ID_PATH, "999999")
                .exchange()
                .expectStatus().isNotFound();
    }

    String createCustomer() {
        String location = webTestClient.post().uri(CustomerRouterConfig.CUSTOMER_PATH)
                .bodyValue(getNewCustomerDto())
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation()
                .toString();

        return location.substring(location.lastIndexOf('/') + 1);
    }

    CustomerDTO getNewCustomerDto() {
        return CustomerDTO.builder()
                .customerName("John Doe")
                .build();
    }
}
