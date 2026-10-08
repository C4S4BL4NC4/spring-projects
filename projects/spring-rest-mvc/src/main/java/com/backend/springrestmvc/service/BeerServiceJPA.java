package com.backend.springrestmvc.service;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.flywaydb.core.internal.util.StringUtils;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public List<BeerDTO> listBeers(String beerName) {
        List<Beer> list;

        if (StringUtils.hasText(beerName)) {
            // do impl
            list = listBeersByName(beerName);

        } else {
            list = beerRepository.findAll();
        }

        return list
                .stream()
                .map(beerMapper::beerToBeerDTO)
                .collect(Collectors.toList());
    }

    private List<Beer> listBeersByName(String beerName) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%");
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        return beerMapper.beerToBeerDTO(beerRepository.save(beerMapper.beerDTOToBeer(beerDTO)));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beerDTO) {
        return beerRepository.findById(id).map(beer -> {
            beer.setBeerName(beerDTO.getBeerName());
            beer.setBeerStyle(beerDTO.getBeerStyle());
            beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            beer.setPrice(beerDTO.getPrice());
            beer.setUpc(beerDTO.getUpc());
            return beerMapper.beerToBeerDTO(beerRepository.save(beer));
        });
    }

    @Override
    public boolean deleteBeer(UUID id) {
        if (beerRepository.existsById(id)) {
            beerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
