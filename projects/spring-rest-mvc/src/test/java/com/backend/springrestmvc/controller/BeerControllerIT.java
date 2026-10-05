package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.repository.BeerRepository;
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
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;

    @Autowired
    BeerMapper beerMapper;

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
        var dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void emptyList() {
        beerRepository.deleteAll();
        var dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(0);
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
    }

    @Test
    void deleteBeerNotFound() {
        assertThrows(NotFoundException.class, () -> beerController.deleteBeer(UUID.randomUUID()));
    }
}
