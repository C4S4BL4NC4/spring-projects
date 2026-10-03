package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.BeerDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BeerService {


    void updateBeer(UUID id, BeerDTO beerDTO);

    List<BeerDTO> getAllBeers();

    Optional<BeerDTO> getBeerById(UUID id);

    BeerDTO saveNewBeer(BeerDTO beerDTO);

    void deleteBeer(UUID beerId);
}
