package com.java.springreactivemongo.repository;

import com.java.springreactivemongo.config.MongoConfig;
import com.java.springreactivemongo.domain.Beer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.mongodb.test.autoconfigure.DataMongoTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@DataMongoTest
@Import(MongoConfig.class)
class BeerRepositoryTest {

    @Autowired
    BeerRepository beerRepository;

    @Test
    void saveNewBeer() {
        AtomicReference<Beer> savedBeer = new AtomicReference<>();

        beerRepository.save(getNewBeer())
                .subscribe(savedBeer::set);

        await().until(() -> savedBeer.get() != null);

        assertThat(savedBeer.get().getId()).isNotNull();
        assertThat(savedBeer.get().getBeerName()).isEqualTo("Efes Malt");
    }

    @Test
    void findSavedBeerById() {
        AtomicReference<Beer> foundBeer = new AtomicReference<>();

        beerRepository.save(getNewBeer())
                .flatMap(saved -> beerRepository.findById(saved.getId()))
                .subscribe(foundBeer::set);

        await().until(() -> foundBeer.get() != null);

        assertThat(foundBeer.get().getBeerName()).isEqualTo("Efes Malt");
    }

    @Test
    void deleteSavedBeer() {
        AtomicBoolean completed = new AtomicBoolean(false);
        AtomicReference<Beer> foundBeer = new AtomicReference<>();

        beerRepository.save(getNewBeer())
                .flatMap(saved -> beerRepository.deleteById(saved.getId())
                        .then(beerRepository.findById(saved.getId())))
                .subscribe(foundBeer::set, null, () -> completed.set(true));

        await().untilTrue(completed);

        assertThat(foundBeer.get()).isNull();
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
