package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.exception.NotFoundException;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.service.BeerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


    @DeleteMapping(BEERS_PATH_ID)
    public ResponseEntity<BeerDTO> deleteBeer(@PathVariable("beerId") UUID beerId) {
        log.debug("Deleting beer with id {}", beerId);
        beerService.deleteBeer(beerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping(BEERS_PATH_ID)
    public ResponseEntity<BeerDTO> updateBeer(@PathVariable("beerId") UUID beerId, @RequestBody BeerDTO beerDTO) {
        log.debug("BeerController updateBeer beerId = " + beerId);
        beerService.updateBeer(beerId, beerDTO);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping(BEERS_PATH)
    public ResponseEntity<BeerDTO> createBeer(@RequestBody BeerDTO beerDTO) {
        BeerDTO savedBeerDTO = beerService.saveNewBeer(beerDTO);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, "/api/v0/beers/" + savedBeerDTO.getId());
        return new ResponseEntity<>(savedBeerDTO, headers, HttpStatus.CREATED);
    }


    @GetMapping(BEERS_PATH)
    public List<BeerDTO> getAllBeers() {
        log.debug("getAllBeers() - in BeerController");
        return beerService.getAllBeers();
    }


    @GetMapping(BEERS_PATH_ID)
    public BeerDTO getBeerById(@PathVariable("beerId") UUID beerId) {
        log.debug("getBeerById() - in BeerController");
        return beerService.getBeerById(beerId).orElseThrow(NotFoundException::new);
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ResponseEntity handleNotFoundException(Exception exception) {
//        return ResponseEntity.notFound().build();
//    }
}
