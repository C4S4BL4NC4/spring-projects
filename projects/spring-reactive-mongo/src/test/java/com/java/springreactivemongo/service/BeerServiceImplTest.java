package com.java.springreactivemongo.service;

import com.java.springreactivemongo.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
class BeerServiceImplTest {

    @Autowired
    BeerService beerService;

    @Test
    void findFirstByBeerName() {
        AtomicReference<BeerDTO> foundBeer = new AtomicReference<>();

        beerService.saveBeer(getNewBeerDto())
                .flatMap(saved -> beerService.findFirstByBeerName(saved.getBeerName()))
                .subscribe(foundBeer::set);

        await().until(() -> foundBeer.get() != null);

        assertThat(foundBeer.get().getId()).isNotNull();
        assertThat(foundBeer.get().getBeerName()).isEqualTo("Efes Malt");
    }

    @Test
    void findByBeerStyle() {
        AtomicReference<List<BeerDTO>> foundBeers = new AtomicReference<>();

        beerService.saveBeer(getNewBeerDto())
                .thenMany(beerService.findAllByBeerStyle("MALT"))
                .collectList()
                .subscribe(foundBeers::set);

        await().until(() -> foundBeers.get() != null);

        assertThat(foundBeers.get()).isNotEmpty();
        assertThat(foundBeers.get()).allMatch(beer -> beer.getBeerStyle().equals("MALT"));
    }

    BeerDTO getNewBeerDto() {
        return BeerDTO.builder()
                .beerName("Efes Malt")
                .beerStyle("MALT")
                .price(new BigDecimal("130.00"))
                .upc("2342134")
                .quantityOnHand(100)
                .build();
    }
}
