package com.java.springreactivemongo.service;

import com.java.springreactivemongo.mapper.BeerMapper;
import com.java.springreactivemongo.model.BeerDTO;
import com.java.springreactivemongo.repository.BeerRepository;
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
    public Flux<BeerDTO> listBeers() {
        return beerRepository.findAll()
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> findById(String id) {
        return beerRepository.findById(id)
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> saveBeer(BeerDTO beerDto) {
        return beerRepository.save(beerMapper.beerDtoToBeer(beerDto))
                .map(beerMapper::beerToBeerDto);
    }

    @Override
    public Mono<BeerDTO> updateBeer(String id, BeerDTO beerDto) {
        return beerRepository.findById(id)
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
    public Mono<BeerDTO> patchBeer(String id, BeerDTO beerDto) {
        return beerRepository.findById(id)
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
    public Mono<Void> deleteBeerById(String id) {
        return beerRepository.deleteById(id);
    }

    @Override
    public Mono<BeerDTO> findFirstByBeerName(String beerName) {
        return beerRepository.findFirstByBeerName(beerName).map(beerMapper::beerToBeerDto);
    }

    @Override
    public Flux<BeerDTO> findAllByBeerStyle(String beerStyle) {
        return beerRepository.findAllByBeerStyle(beerStyle).map(beerMapper::beerToBeerDto);
    }
}
