package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.model.BeerStyle;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    Page<Beer> findAllByBeerNameIsLikeIgnoreCase(String beerName, Pageable pageable);

    Page<Beer> findAllByBeerStyle(BeerStyle beerStyle, Pageable pageable);

    Page<Beer> findAllByBeerStyleAndBeerNameIsLikeIgnoreCase(BeerStyle beerStyle, String beerName, Pageable pageable);
}
