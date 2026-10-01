package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {


    void updateBeer(UUID id, Beer beer);

    List<Beer> getAllBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);

    void deleteBeer(UUID beerId);
}
