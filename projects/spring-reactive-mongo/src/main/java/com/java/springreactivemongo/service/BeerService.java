package com.java.springreactivemongo.service;

import com.java.springreactivemongo.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {
    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> findById(String id);

    Mono<BeerDTO> saveBeer(BeerDTO beerDto);

    Mono<BeerDTO> updateBeer(String id, BeerDTO beerDto);

    Mono<BeerDTO> patchBeer(String id, BeerDTO beerDto);

    Mono<Void> deleteBeerById(String id);

    Mono<BeerDTO> findFirstByBeerName(String beerName);

    Flux<BeerDTO> findAllByBeerStyle(String beerStyle);
}
