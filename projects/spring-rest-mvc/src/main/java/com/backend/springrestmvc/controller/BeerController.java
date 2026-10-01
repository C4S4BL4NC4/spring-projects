package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.Beer;
import com.backend.springrestmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@AllArgsConstructor
@RestController     // Paired with @RequestBody for JSON returning purposes.
//@Controller      // Can return any type of data used when to for example wanna render server sided HTML.
@RequestMapping("/api/v0/beers")
public class BeerController {
    private final BeerService beerService;

    
    @PostMapping
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);
        return ResponseEntity.ok().body(savedBeer);
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> getAllBeers() {
        log.info("getAllBeers() - in BeerController");
        return beerService.getAllBeers();
    }


    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("getBeerById() - in BeerController");
        return beerService.getBeerById(beerId);
    }
}
