package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.CustomerDTO;
import com.backend.springrestmvc.service.CustomerService;
import com.backend.springrestmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl;

    CustomerDTO customerDTO;

    @BeforeEach
    void setUp() {
        customerServiceImpl = new CustomerServiceImpl();

        customerDTO = CustomerDTO.builder()
                .id(UUID.randomUUID())
                .version(0)
                .name("Bill Murray")
                .createdDate(LocalDateTime.now())
                .build();
    }

    @Test
    void deleteCustomer() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.getAllCustomers().get(0);
        given(customerService.deleteCustomer(any(UUID.class))).willReturn(true);

        mockMvc.perform(
                        delete(
                                CustomerController.CUSTOMERS_PATH_ID, customerDTO.getId().toString()
                        ).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(customerService).deleteCustomer(uuidArgumentCaptor.capture());
        assertThat(customerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());

    }

    @Test
    void deleteCustomerNotFound() throws Exception {
        given(customerService.deleteCustomer(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(CustomerController.CUSTOMERS_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomerNotFound() throws Exception {
        given(customerService.updateCustomer(any(UUID.class), any(CustomerDTO.class))).willReturn(Optional.empty());

        mockMvc.perform(put(CustomerController.CUSTOMERS_PATH_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateCustomer() throws Exception {
        CustomerDTO customerDTO = customerServiceImpl.getAllCustomers().get(0);
        given(customerService.updateCustomer(any(UUID.class), any(CustomerDTO.class))).willReturn(Optional.of(customerDTO));

        mockMvc.perform(
                put(
                        CustomerController.CUSTOMERS_PATH_ID, customerDTO.getId().toString()
                ).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(customerDTO))
        ).andExpect(status().isNoContent());

        verify(customerService).updateCustomer(any(UUID.class), any(CustomerDTO.class));
    }

    @Test
    void createCustomer() throws Exception {

        given(customerService.createCustomer(any(CustomerDTO.class))).willReturn(customerDTO);

        mockMvc.perform(
                        post(
                                CustomerController.CUSTOMERS_PATH)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(customerDTO))
                )
                .andExpect(status().isCreated());
    }

    @Test
    void getAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        var result = mockMvc.perform(
                        get(
                                CustomerController.CUSTOMERS_PATH
                        ).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void getCustomerById() throws Exception {

        var customerId = customerDTO.getId();

        given(customerService.getCustomerById(customerId))
                .willReturn(Optional.ofNullable(customerDTO));

        var result = mockMvc.perform(
                        get(
                                CustomerController.CUSTOMERS_PATH_ID, customerDTO.getId().toString()
                        ).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }

    @Test
    void getCustomerIdNotFound() throws Exception {

        given(customerService.getCustomerById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(CustomerController.CUSTOMERS_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }
}