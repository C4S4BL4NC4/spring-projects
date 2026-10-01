package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.model.Beer;
import com.backend.springrestmvc.service.BeerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

    @DeleteMapping("/{beerId}")
    public ResponseEntity<Beer> deleteBeer(@PathVariable("beerId") UUID beerId) {
        log.debug("Deleting beer with id {}", beerId);
        beerService.deleteBeer(beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity<Beer> updateBeer(@PathVariable("beerId") UUID beerId,  @RequestBody Beer beer) {
        log.debug("BeerController updateBeer beerId = " + beerId);
        beerService.updateBeer(beerId, beer);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping
    public ResponseEntity<Beer> createBeer(@RequestBody Beer beer) {
        Beer savedBeer = beerService.saveNewBeer(beer);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v0/beers/" + savedBeer.getId());
        return new ResponseEntity<>(savedBeer, headers, HttpStatus.CREATED);
    }


    @RequestMapping(method = RequestMethod.GET)
    public List<Beer> getAllBeers() {
        log.debug("getAllBeers() - in BeerController");
        return beerService.getAllBeers();
    }


    @RequestMapping(value = "/{beerId}", method = RequestMethod.GET)
    public Beer getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("getBeerById() - in BeerController");
        return beerService.getBeerById(beerId);
    }
}
