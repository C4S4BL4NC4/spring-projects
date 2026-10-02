package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.Customer;
import com.backend.springrestmvc.service.CustomerService;
import com.backend.springrestmvc.service.CustomerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    CustomerService customerService;

    CustomerServiceImpl customerServiceImpl = new CustomerServiceImpl();

    @Test
    void getAllCustomers() throws Exception {
        given(customerService.getAllCustomers()).willReturn(customerServiceImpl.getAllCustomers());

        var result = mockMvc.perform(get("/api/v0/customers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }

    @Test
    void getCustomerById() throws Exception {

        var testCustomer = Customer.builder()
                .id(UUID.randomUUID())
                .name("Ibrahim Tatlises")
                .version(0)
                .createdDate(LocalDateTime.now().minusDays(60))
                .lastModifiedDate(LocalDateTime.now().minusDays(2))
                .build();

        var customerId = testCustomer.getId();

        given(customerService.getCustomerById(customerId))
                .willReturn(testCustomer);

        var result = mockMvc.perform(get("/api/v0/customers/" + customerId).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
    }
}