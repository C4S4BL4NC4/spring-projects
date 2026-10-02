package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.Beer;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.model.Customer;
import com.backend.springrestmvc.service.BeerService;
import com.backend.springrestmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl;

    Beer beer;

    String beersPath = "/api/v0/beers";

    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();

        beer = Beer.builder()
                .id(UUID.randomUUID())
                .beerName("Guinness")
                .beerStyle(BeerStyle.STOUT)
                .quantityOnHand(20)
                .price(BigDecimal.valueOf(150.00))
                .createdAt(LocalDateTime.now())
                .version(0)
                .upc("555555")
                .build();
    }

    @Test
    void deleteBeer() throws Exception {
        Beer beer = beerServiceImpl.getAllBeers().get(0);

        mockMvc.perform(
                        delete(
                                beersPath + '/' + beer.getId()
                        ).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeer(uuidArgumentCaptor.capture());
        assertThat(beer.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void createBeer() throws Exception {

        given(beerService.saveNewBeer(any(Beer.class))).willReturn(beer);

        mockMvc.perform(
                        post(beersPath)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beer))
                ).andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateBeer() throws Exception {
        Beer beer = beerServiceImpl.getAllBeers().get(0);

        mockMvc.perform(
                put(
                        beersPath + '/' + beer.getId()
                ).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beer))
        ).andExpect(status().isNoContent());

        verify(beerService).updateBeer(any(UUID.class), any(Beer.class));
    }

    @Test
    void getBeers() throws Exception {
        given(beerService.getAllBeers()).willReturn(beerServiceImpl.getAllBeers());

        mockMvc.perform(
                        get(beersPath)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {

        UUID beerId = beer.getId();

        given(beerService.getBeerById(beerId))
                .willReturn(beer);

        var result = mockMvc.perform(get(beersPath + '/' + beerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(beer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beer.getBeerName().toString())))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}