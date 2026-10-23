package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.config.TestJwt;
import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.event.BeerCreatedEvent;
import com.backend.springrestmvc.event.BeerDeletedEvent;
import com.backend.springrestmvc.event.BeerPatchedEvent;
import com.backend.springrestmvc.event.BeerUpdatedEvent;
import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.repository.BeerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.event.ApplicationEvents;
import org.springframework.test.context.event.RecordApplicationEvents;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import tools.jackson.databind.ObjectMapper;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RecordApplicationEvents
@SpringBootTest
class BeerControllerIT {

    @Autowired
    ApplicationEvents applicationEvents;

    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

    @Autowired
    WebApplicationContext wac;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    CacheManager cacheManager;

    MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Tests change the database through the repository, bypassing cache eviction
        cacheManager.getCacheNames().forEach(name -> cacheManager.getCache(name).clear());

        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .apply(springSecurity())
                .build();
    }

    @Test
    void testUnauthorized() throws Exception {
        mockMvc.perform(get(BeerController.BEERS_PATH)
                        .queryParam("beerName", "IPA"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testListBeersPaging() throws Exception {
        mockMvc.perform(get(BeerController.BEERS_PATH)
                        .queryParam("beerName", "IPA")
                        .queryParam("beerStyle", BeerStyle.IPA.name())
                        .queryParam("pageNumber", "2")
                        .queryParam("pageSize", "50")
                        .with(TestJwt.JWT))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()", is(50)));
    }

    @Test
    void testListBeersByName() throws Exception {
        mockMvc.perform(
                        get(BeerController.BEERS_PATH)
                                .queryParam("beerName", "IPA")
                                .with(TestJwt.JWT)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(336)));

    }

    @Test
    void testListBeersByStyle() throws Exception {
        mockMvc.perform(
                        get(BeerController.BEERS_PATH)
                                .queryParam("beerStyle", BeerStyle.IPA.name())
                                .with(TestJwt.JWT)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(547)));

    }

    @Test
    void testListBeerByStyleAndBeerName() throws Exception {
        mockMvc.perform(
                        get(BeerController.BEERS_PATH)
                                .queryParam("beerStyle", BeerStyle.IPA.name())
                                .queryParam("beerName", "IPA")
                                .with(TestJwt.JWT)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElements", is(310)));
    }

    @Test
    void updateBeerNameTooLong() throws Exception {
        Beer beer = beerRepository.findAll().getFirst();
        BeerDTO dto = beerMapper.beerToBeerDTO(beer);
        dto.setId(null);
        dto.setVersion(null);
        dto.setBeerName("Beer".repeat(20));

        mockMvc.perform(put(BeerController.BEERS_PATH_ID, beer.getId())
                        .with(TestJwt.JWT)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$[0].beerName").exists());
    }

    @Test
    void beerByIdNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.getBeerById(UUID.randomUUID()));
    }

    @Test
    void getBeerById() {
        Beer beer = beerRepository.findAll().get(0);
        BeerDTO beerDTO = beerController.getBeerById(beer.getId());
        assertThat(beerDTO).isNotNull();
    }

    @Test
    void getAllBeers() {
        var dtos = beerController.listBeers(null, null, 1, 25);
        assertThat(dtos.getTotalElements()).isGreaterThan(2100);
    }

    @Test
    void listBeersSortedByNameDesc() {
        var dtos = beerController.listBeers(null, null, 1, 25);

        var expectedNames = beerRepository.findAll(Sort.by(Sort.Order.desc("beerName"))).stream()
                .limit(25)
                .map(Beer::getBeerName)
                .toList();

        assertThat(dtos.getContent()).extracting(BeerDTO::getBeerName).isEqualTo(expectedNames);
    }

    @Rollback
    @Transactional
    @Test
    void emptyList() {
        beerRepository.deleteAll();
        var dtos = beerController.listBeers(null, null, 1, 25);
        assertThat(dtos.getTotalElements()).isEqualTo(0);
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeer() {
        var dto = BeerDTO.builder().beerName("Budweiser").build();
        var responseEntity = beerController.createBeer(dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getHeaders().getLocation()).isNotNull();
        var uuid = UUID.fromString(
                responseEntity
                        .getHeaders()
                        .getLocation()
                        .getPath()
                        .split("/")[4]
        );
        var beer = beerRepository.findById(uuid);
        assertThat(beer).isNotNull();
        Assertions.assertEquals(1, applicationEvents
                .stream(BeerCreatedEvent.class)
                .count());
    }

    @Rollback
    @Transactional
    @Test
    void updateBeer() {
        var beer = beerRepository.findAll().getFirst();
        var dto = BeerDTO.builder().id(null).version(null).beerName("Budweiser").build();
        var responseEntity = beerController.updateBeer(beer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beer.getBeerName()).isEqualTo("Budweiser");
        Assertions.assertEquals(1, applicationEvents
                .stream(BeerUpdatedEvent.class)
                .count());
    }

    @Rollback
    @Transactional
    @Test
    void patchBeer() {
        var beer = beerRepository.findAll().getFirst();
        var originalStyle = beer.getBeerStyle();
        var dto = BeerDTO.builder().beerName("Patched").build();
        var responseEntity = beerController.patchBeerById(beer.getId(), dto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beer.getBeerName()).isEqualTo("Patched");
        assertThat(beer.getBeerStyle()).isEqualTo(originalStyle);
        Assertions.assertEquals(1, applicationEvents
                .stream(BeerPatchedEvent.class)
                .count());
    }

    @Test
    void patchBeerNotFound() {
        var dto = BeerDTO.builder().beerName("Patched").build();
        assertThrows(NotFoundException.class, () -> beerController.patchBeerById(UUID.randomUUID(), dto));
    }

    @Test
    void updateBeerNotFound() {
        BeerDTO dto = beerMapper.beerToBeerDTO(beerRepository.findAll().getFirst());
        dto.setId(null);
        dto.setVersion(null);
        dto.setBeerName("Budweiser");

        assertThrows(NotFoundException.class, () -> beerController.updateBeer(UUID.randomUUID(), dto));
    }

    @Rollback
    @Transactional
    @Test
    void deleteBeer() {
        var beer = beerRepository.findAll().getFirst();
        var responseEntity = beerController.deleteBeer(beer.getId());
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(beerRepository.findById(beer.getId())).isEmpty();
        Assertions.assertEquals(1, applicationEvents
                .stream(BeerDeletedEvent.class)
                .count());
    }

    @Test
    void deleteBeerNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.deleteBeer(UUID.randomUUID()));
    }
}
