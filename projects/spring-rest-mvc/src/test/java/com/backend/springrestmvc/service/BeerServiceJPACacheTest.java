package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.repository.BeerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.clearInvocations;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
class BeerServiceJPACacheTest {

    @Autowired
    BeerService beerService;

    @Autowired
    CacheManager cacheManager;

    @MockitoSpyBean
    BeerRepository beerRepository;

    UUID beerId;

    @BeforeEach
    void setUp() {
        cacheManager.getCache("beerCache").clear();
        cacheManager.getCache("beerListCache").clear();
        beerId = beerRepository.findAll().getFirst().getId();
        clearInvocations(beerRepository);
    }

    @Test
    void getBeerByIdIsCached() {
        var first = beerService.getBeerById(beerId);
        var second = beerService.getBeerById(beerId);

        assertThat(second).isEqualTo(first);
        verify(beerRepository, times(1)).findById(beerId);
    }

    @Test
    void listBeersIsCached() {
        beerService.listBeers(null, null, 1, 25);
        beerService.listBeers(null, null, 1, 25);

        verify(beerRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void listBeersCachesEachArgumentCombinationSeparately() {
        beerService.listBeers(null, null, 1, 25);
        beerService.listBeers(null, null, 2, 25);

        verify(beerRepository, times(2)).findAll(any(Pageable.class));
    }

    @Rollback
    @Transactional
    @Test
    void saveNewBeerClearsListCache() {
        beerService.listBeers(null, null, 1, 25);

        beerService.saveNewBeer(BeerDTO.builder()
                .beerName("Cache Test Beer")
                .beerStyle(BeerStyle.IPA)
                .upc("123456")
                .price(new BigDecimal("9.99"))
                .build());
        beerService.listBeers(null, null, 1, 25);

        verify(beerRepository, times(2)).findAll(any(Pageable.class));
    }

    @Rollback
    @Transactional
    @Test
    void updateBeerEvictsBeerAndClearsListCache() {
        beerService.getBeerById(beerId);
        beerService.listBeers(null, null, 1, 25);

        // Build a fresh DTO so the cached instance isn't mutated
        var cached = beerService.getBeerById(beerId).orElseThrow();
        beerService.updateBeer(beerId, BeerDTO.builder()
                .beerName("Updated Name")
                .beerStyle(cached.getBeerStyle())
                .upc(cached.getUpc())
                .quantityOnHand(cached.getQuantityOnHand())
                .price(cached.getPrice())
                .build());

        assertThat(cacheManager.getCache("beerCache").get(beerId)).isNull();
        assertThat(beerService.getBeerById(beerId).orElseThrow().getBeerName()).isEqualTo("Updated Name");

        beerService.listBeers(null, null, 1, 25);
        verify(beerRepository, times(2)).findAll(any(Pageable.class));
    }

    @Rollback
    @Transactional
    @Test
    void deleteBeerEvictsBeerAndClearsListCache() {
        beerService.getBeerById(beerId);
        beerService.listBeers(null, null, 1, 25);

        assertThat(beerService.deleteBeer(beerId)).isTrue();

        assertThat(cacheManager.getCache("beerCache").get(beerId)).isNull();
        assertThat(beerService.getBeerById(beerId)).isEmpty();

        beerService.listBeers(null, null, 1, 25);
        verify(beerRepository, times(2)).findAll(any(Pageable.class));
    }

    @Test
    void deleteMissingBeerKeepsCaches() {
        beerService.getBeerById(beerId);
        beerService.listBeers(null, null, 1, 25);

        assertThat(beerService.deleteBeer(UUID.randomUUID())).isFalse();

        beerService.getBeerById(beerId);
        beerService.listBeers(null, null, 1, 25);
        verify(beerRepository, times(1)).findById(beerId);
        verify(beerRepository, times(1)).findAll(any(Pageable.class));
    }
}
