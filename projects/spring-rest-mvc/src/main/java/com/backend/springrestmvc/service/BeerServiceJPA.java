package com.backend.springrestmvc.service;

import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;

    private final BeerMapper beerMapper;

    @Override
    public void updateBeer(UUID id, BeerDTO beerDTO) {

    }

    @Override
    public List<BeerDTO> getAllBeers() {
        return List.of();
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        return Optional.empty();
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return null;
    }

    @Override
    public void deleteBeer(UUID beerId) {

    }
}
