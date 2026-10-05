package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@RestController     // Paired with @RequestBody for JSON returning purposes.
//@Controller      // Can return any type of data used when to for example wanna render server sided HTML.
public class BeerController {
    public static final String BEERS_PATH = "/api/v0/beers";
    public static final String BEERS_PATH_ID = BEERS_PATH + "/{beerId}";

    private final BeerService beerService;

    @GetMapping(BEERS_PATH)
    public List<BeerDTO> getAllBeers() {
        log.debug("getAllBeers() - in BeerController");
        return beerService.getAllBeers();
    }

    @GetMapping(BEERS_PATH_ID)
    public BeerDTO getBeerById(@PathVariable UUID beerId) {
        log.debug("getBeerById() - in BeerController, beerId = {}", beerId);
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

    @PostMapping(BEERS_PATH)
    public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beerDTO) {
        log.debug("createBeer() - in BeerController");
        BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);
        return ResponseEntity
                .created(URI.create(BEERS_PATH + "/" + savedBeerDTO.getId()))
                .body(savedBeerDTO);
    }

    @PutMapping(BEERS_PATH_ID)
    public ResponseEntity<Void> updateBeer(@PathVariable UUID beerId, @RequestBody BeerDTO beerDTO) {
        log.debug("updateBeer() - in BeerController, beerId = {}", beerId);
        beerService.updateBeer(beerId, beerDTO).orElseThrow(NotFoundException::new);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(BEERS_PATH_ID)
    public ResponseEntity<Void> deleteBeer(@PathVariable UUID beerId) {
        log.debug("deleteBeer() - in BeerController, beerId = {}", beerId);
        if (!beerService.deleteBeer(beerId)) {
            throw new NotFoundException();
        }
        return ResponseEntity.noContent().build();
    }
}
