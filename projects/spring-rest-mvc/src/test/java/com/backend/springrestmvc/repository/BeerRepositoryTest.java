package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.bootstrap.BootstrapData;
import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.service.BeerCsvServiceImpl;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import({BootstrapData.class, BeerCsvServiceImpl.class})
class BeerRepositoryTest {

    Beer beer;
    @Autowired
    private BeerRepository beerRepository;

    @BeforeEach
    void setUp() {
        beer = Beer.builder()
                .beerName("Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("12345")
                .price(new BigDecimal("130.00"))
                .build();
    }

    @Test
    void testGetBeerLisByName() {
        Page<Beer> page = beerRepository.findAllByBeerNameIsLikeIgnoreCase("%IPA%", Pageable.unpaged());
        assertThat(page.getContent().size()).isEqualTo(336);
    }

    @Test
    void testSaveBeer() {
        var savedBeer = beerRepository.save(beer);

        beerRepository.flush();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
    }

    @Test
    void testExceedBeerNameLength() {

        assertThrows(ConstraintViolationException.class, () -> {
            var savedBeer = beerRepository.save(
                    Beer.builder()
                            .beerName("Beer".repeat(50))
                            .beerStyle(BeerStyle.IPA)
                            .upc("12345")
                            .price(new BigDecimal("130.00"))
                            .build()
            );
            beerRepository.flush();
        });
    }
}