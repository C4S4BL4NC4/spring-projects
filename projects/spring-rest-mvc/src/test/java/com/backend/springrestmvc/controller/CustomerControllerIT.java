package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.entity.Customer;
import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.mapper.CustomerMapper;
import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
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

    @Autowired
    CustomerMapper customerMapper;

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

    @Rollback
    @Transactional
    @Test
    void createCustomer() {
        var dto = CustomerDTO.builder().name("New Customer").build();
        var responseEntity = customerController.createCustomer(dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();

        String[] pathParts = responseEntity.getHeaders().getLocation().getPath().split("/");
        var savedId = UUID.fromString(pathParts[pathParts.length - 1]);
        assertThat(customerRepository.findById(savedId)).isPresent();
    }

    @Rollback
    @Transactional
    @Test
    void updateCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        CustomerDTO dto = customerMapper.customerToCustomerDTO(customer);
        dto.setId(null);
        dto.setVersion(null);
        dto.setName("UPDATED");

        var responseEntity = customerController.updateCustomer(customer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId()).get().getName()).isEqualTo("UPDATED");
    }

    @Test
    void updateCustomerNotFound() {
        var dto = CustomerDTO.builder().name("Nobody").build();
        assertThrows(NotFoundException.class, () -> customerController.updateCustomer(UUID.randomUUID(), dto));
    }

    @Rollback
    @Transactional
    @Test
    void deleteCustomer() {
        Customer customer = customerRepository.findAll().getFirst();
        var responseEntity = customerController.deleteCustomer(customer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(customerRepository.findById(customer.getId())).isEmpty();
    }

    @Test
    void deleteCustomerNotFound() {
        assertThrows(NotFoundException.class, () -> customerController.deleteCustomer(UUID.randomUUID()));
    }
}
