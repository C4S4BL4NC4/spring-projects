package com.backend.springrestmvc.service;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;


    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {


        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyleAndBeerName(beerName, beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        
        return beerPage.map(beerMapper::beerToBeerDTO);
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

    // Filtering Helper Methods
    private Page<Beer> listBeersByStyleAndBeerName(String beerName, BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyleAndBeerNameIsLikeIgnoreCase(beerStyle, "%" + beerName + "%", pageable);
    }

    public Page<Beer> listBeersByStyle(BeerStyle beerStyle, Pageable pageable) {
        return beerRepository.findAllByBeerStyle(beerStyle, pageable);
    }

    public Page<Beer> listBeersByName(String beerName, Pageable pageable) {
        return beerRepository.findAllByBeerNameIsLikeIgnoreCase("%" + beerName + "%", pageable);
    }

    private Page<Beer> findAllBeers(PageRequest pageRequest) {
        return beerRepository.findAll(pageRequest);
    }

    // Pagination Helper Methods
    private PageRequest buildPageRequest(Integer pageNumber, Integer pageSize) {
        int queryPageNumber = pageNumber == null || pageNumber <= 0 ? DEFAULT_PAGE_NUMBER : pageNumber - 1;
        // set maximum page size constant later
        int queryPageSize = pageSize == null || pageSize <= 0 || pageSize > 1000 ? DEFAULT_PAGE_SIZE : pageSize;

        Sort sort = Sort.by(Sort.Order.desc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
