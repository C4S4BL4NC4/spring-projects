package com.backend.springreactive.controller;

import com.backend.springreactive.model.BeerDTO;
import com.backend.springreactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {
    private static final String BEER_URL = "/api/v2/beers";
    private static final String BEER_URL_ID = BEER_URL + "/{beerId}";


    private final BeerService beerService;

    @GetMapping(BEER_URL)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_URL_ID)
    public Mono<BeerDTO> getBeer(@PathVariable Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PostMapping(BEER_URL)
    public Mono<BeerDTO> saveBeer(@Validated @RequestBody BeerDTO beerDto) {
        return beerService.createBeer(beerDto);
    }

    @PutMapping(BEER_URL_ID)
    public Mono<BeerDTO> updateBeer(@PathVariable Integer beerId, @Validated @RequestBody BeerDTO beerDto) {
        return beerService.updateBeer(beerId, beerDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @PatchMapping(BEER_URL_ID)
    public Mono<BeerDTO> patchBeer(@PathVariable Integer beerId, @RequestBody BeerDTO beerDto) {
        return beerService.patchBeer(beerId, beerDto)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    @DeleteMapping(BEER_URL_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteBeer(@PathVariable Integer beerId) {
        return beerService.getBeerById(beerId)
                .switchIfEmpty(Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND)))
                .flatMap(foundBeer -> beerService.deleteBeerById(foundBeer.getId()));
    }


}
