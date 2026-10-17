package com.backend.springreactive.controller;

import com.backend.springreactive.mapper.BeerMapper;
import com.backend.springreactive.model.BeerDTO;
import com.backend.springreactive.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequiredArgsConstructor
public class BeerController {
    private static final String API_URL = "/api/v1/beers";


    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @GetMapping(API_URL)
    public Flux<BeerDTO> getBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDTO);
    }

}
