package com.backend.springreactive.controller;

import com.backend.springreactive.model.BeerDTO;
import com.backend.springreactive.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class BeerController {
    private static final String BEER_URL = "/api/v0/beers";
    private static final String BEER_URL_ID = BEER_URL + "/{beerId}";


    private final BeerService beerService;

    @GetMapping(BEER_URL)
    public Flux<BeerDTO> listBeers() {
        return beerService.listBeers();
    }

    @GetMapping(BEER_URL_ID)
    public Mono<BeerDTO> getBeer(@PathVariable Integer beerId) {
        return beerService.getBeerById(beerId);
    }


}
