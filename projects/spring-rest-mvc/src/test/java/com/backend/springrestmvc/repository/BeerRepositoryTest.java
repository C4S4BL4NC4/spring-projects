package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.entity.Beer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class BeerRepositoryTest {

    Beer beer;
    @Autowired
    private BeerRepository beerRepository;

    @BeforeEach
    void setUp() {
        beer = Beer.builder().beerName("Beer").build();
    }

    @Test
    void testSaveBeer() {
        var savedBeer = beerRepository.save(beer);
        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }
}