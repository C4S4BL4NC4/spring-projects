package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.Beer;

import java.util.List;
import java.util.UUID;

public interface BeerService {

    List<Beer> getAllBeers();

    Beer getBeerById(UUID id);

    Beer saveNewBeer(Beer beer);
}
