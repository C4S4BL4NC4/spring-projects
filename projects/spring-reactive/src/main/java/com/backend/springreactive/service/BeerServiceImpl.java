package com.backend.springreactive.service;

import com.backend.springreactive.domain.Beer;
import com.backend.springreactive.mapper.BeerMapper;
import com.backend.springreactive.model.BeerDTO;
import com.backend.springreactive.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BeerServiceImpl implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Mono<BeerDTO> getBeerById(Integer beerId) {
        return beerRepository.findById(beerId)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> createBeer(BeerDTO beerDto) {
        Mono<Beer> savedBeer = beerRepository.save(beerMapper.beerDtoToBeer(beerDto));

        return savedBeer.map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> updateBeer(Integer beerId, BeerDTO beerDto) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    foundBeer.setBeerName(beerDto.getBeerName());
                    foundBeer.setBeerStyle(beerDto.getBeerStyle());
                    foundBeer.setUpc(beerDto.getUpc());
                    foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
                    foundBeer.setPrice(beerDto.getPrice());
                    return foundBeer;
                })
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> patchBeer(Integer beerId, BeerDTO beerDto) {
        return beerRepository.findById(beerId)
                .map(foundBeer -> {
                    if (beerDto.getBeerName() != null) {
                        foundBeer.setBeerName(beerDto.getBeerName());
                    }
                    if (beerDto.getBeerStyle() != null) {
                        foundBeer.setBeerStyle(beerDto.getBeerStyle());
                    }
                    if (beerDto.getUpc() != null) {
                        foundBeer.setUpc(beerDto.getUpc());
                    }
                    if (beerDto.getQuantityOnHand() != null) {
                        foundBeer.setQuantityOnHand(beerDto.getQuantityOnHand());
                    }
                    if (beerDto.getPrice() != null) {
                        foundBeer.setPrice(beerDto.getPrice());
                    }
                    return foundBeer;
                })
                .flatMap(beerRepository::save)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<Void> deleteBeerById(Integer beerId) {
        return beerRepository.deleteById(beerId);
    }

    @Override
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDto);
    }
}