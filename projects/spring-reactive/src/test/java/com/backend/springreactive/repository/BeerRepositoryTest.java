package com.backend.springreactive.repository;

import com.backend.springreactive.config.DatabaseConfig;
import com.backend.springreactive.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.r2dbc.test.autoconfigure.DataR2dbcTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;

@DataR2dbcTest
@Import(DatabaseConfig.class)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void saveNewBeer() {
        beerRepository.save(getNewBeer())
                .subscribe(beer -> System.out.println(beer.toString()));
    }

    Beer getNewBeer() {
        return Beer.builder()
                .beerName("Efes Malt")
                .beerStyle("MALT")
                .price(new BigDecimal("130.00"))
                .upc("2342134")
                .quantityOnHand(100)
                .build();
    }
}