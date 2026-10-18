package com.backend.springreactive.service;

import com.backend.springreactive.model.BeerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BeerService {

    Flux<BeerDTO> listBeers();

    Mono<BeerDTO> getBeerById(Integer beerId);

    Mono<BeerDTO> createBeer(BeerDTO beerDto);

    Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDto);

    Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDto);

    Mono<Void> deleteBeerById(Integer beerId);
}
