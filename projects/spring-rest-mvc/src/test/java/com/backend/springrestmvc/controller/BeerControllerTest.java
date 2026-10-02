package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.Beer;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.service.BeerService;
import com.backend.springrestmvc.service.BeerServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BeerController.class)
class BeerControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    BeerService beerService;

    BeerServiceImpl beerServiceImpl = new BeerServiceImpl();

    @Test
    void getBeers() throws Exception {
        given(beerService.getAllBeers()).willReturn(beerServiceImpl.getAllBeers());

        mockMvc.perform(
                        get("/api/v0/beers")
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {

        Beer testBeer = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("EFES")
                .beerStyle(BeerStyle.MALT)
                .upc("222222")
                .quantityOnHand(110)
                .price(BigDecimal.valueOf(120.00))
                .createdAt(LocalDateTime.now().minusDays(3))
                .updatedAt(LocalDateTime.now().minusMinutes(20))
                .build();

        UUID beerId = testBeer.getId();

        given(beerService.getBeerById(beerId))
                .willReturn(testBeer);

        var result = mockMvc.perform(get("/api/v0/beers/" + beerId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(testBeer.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(testBeer.getBeerName().toString())))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}