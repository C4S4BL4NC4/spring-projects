package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.model.BeerStyle;
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
import java.util.Optional;
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

    BeerDTO beerDTO;


    @BeforeEach
    void setUp() {
        beerServiceImpl = new BeerServiceImpl();

        beerDTO = BeerDTO.builder()
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
    void getBeerIdNotFound() throws Exception {

        given(beerService.getBeerById(any())).willReturn(Optional.empty());

        mockMvc.perform(get(BeerController.BEERS_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteBeer() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, 1, 25).getContent().get(0);
        given(beerService.deleteBeer(any(UUID.class))).willReturn(true);

        mockMvc.perform(
                        delete(
                                BeerController.BEERS_PATH_ID, beerDTO.getId().toString()
                        ).accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        ArgumentCaptor<UUID> uuidArgumentCaptor = ArgumentCaptor.forClass(UUID.class);
        verify(beerService).deleteBeer(uuidArgumentCaptor.capture());
        assertThat(beerDTO.getId()).isEqualTo(uuidArgumentCaptor.getValue());
    }

    @Test
    void deleteBeerNotFound() throws Exception {
        given(beerService.deleteBeer(any(UUID.class))).willReturn(false);

        mockMvc.perform(delete(BeerController.BEERS_PATH_ID, UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBeerNotFound() throws Exception {
        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.empty());

        mockMvc.perform(put(BeerController.BEERS_PATH_ID, UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    void createBeer() throws Exception {

        given(beerService.saveNewBeer(any(BeerDTO.class))).willReturn(beerDTO);

        mockMvc.perform(
                        post(BeerController.BEERS_PATH)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(beerDTO))
                ).andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    @Test
    void updateBeer() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, 1, 25).getContent().getFirst();
        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beerDTO));

        mockMvc.perform(
                put(
                        BeerController.BEERS_PATH_ID, beerDTO.getId().toString()
                ).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
        ).andExpect(status().isNoContent());

        verify(beerService).updateBeer(any(UUID.class), any(BeerDTO.class));
    }

    @Test
    void updateBeerBlankFields() throws Exception {
        BeerDTO beerDTO = beerServiceImpl.listBeers(null, null, 1, 25).getContent().getFirst();
        beerDTO.setBeerStyle(null);
        beerDTO.setBeerName("");
        beerDTO.setUpc(null);

        given(beerService.updateBeer(any(UUID.class), any(BeerDTO.class))).willReturn(Optional.of(beerDTO));

        mockMvc.perform(
                put(
                        BeerController.BEERS_PATH_ID, beerDTO.getId().toString()
                ).accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(beerDTO))
        ).andExpect(status().isBadRequest());

    }

    @Test
    void getBeers() throws Exception {
        given(beerService.listBeers(any(), any(), any(), any())).willReturn(beerServiceImpl.listBeers(null, null, 1, 25));

        mockMvc.perform(
                        get(BeerController.BEERS_PATH)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content.length()", is(3)));
    }

    @Test
    void getBeerById() throws Exception {

        UUID beerId = beerDTO.getId();

        given(beerService.getBeerById(beerId))
                .willReturn(Optional.of(beerDTO));

        var result = mockMvc.perform(get(BeerController.BEERS_PATH_ID, beerDTO.getId().toString())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(beerDTO.getId().toString())))
                .andExpect(jsonPath("$.beerName", is(beerDTO.getBeerName())))
                .andReturn();

        System.out.println(result.getResponse().getContentAsString());
    }
}