package com.backend.springrestmvc.service;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.event.BeerCreatedEvent;
import com.backend.springrestmvc.event.BeerDeletedEvent;
import com.backend.springrestmvc.event.BeerPatchedEvent;
import com.backend.springrestmvc.event.BeerUpdatedEvent;
import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.repository.BeerRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class BeerServiceJPA implements BeerService {

    private final static int DEFAULT_PAGE_NUMBER = 0;
    private final static int DEFAULT_PAGE_SIZE = 25;


    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;
    private final ConcurrentMapCacheManager cacheManager;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Cacheable(cacheNames = "beerListCache")
    @Override
    public Page<BeerDTO> listBeers(String beerName, BeerStyle beerStyle, Integer pageNumber, Integer pageSize) {

        log.info("listBeers - in BeerServiceJPA");

        PageRequest pageRequest = buildPageRequest(pageNumber, pageSize);

        Page<Beer> beerPage;

        if (StringUtils.hasText(beerName) && beerStyle == null) {
            beerPage = listBeersByName(beerName, pageRequest);
        } else if (!StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyle(beerStyle, pageRequest);
        } else if (StringUtils.hasText(beerName) && beerStyle != null) {
            beerPage = listBeersByStyleAndBeerName(beerName, beerStyle, pageRequest);
        } else {
            beerPage = findAllBeers(pageRequest);
        }

        return beerPage.map(beerMapper::beerToBeerDTO);
    }


    @Cacheable(cacheNames = "beerCache", key = "#id")
    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {

        return Optional.ofNullable(beerMapper.beerToBeerDTO(beerRepository.findById(id)
                .orElse(null)));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {

        cacheManager.getCache("beerListCache").clear();

        var savedBeer = beerRepository.save(beerMapper.beerDTOToBeer(beerDTO));
        var auth = SecurityContextHolder.getContext().getAuthentication();
        applicationEventPublisher.publishEvent(new BeerCreatedEvent(savedBeer, auth));

        return beerMapper.beerToBeerDTO(beerRepository.save(savedBeer));
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beerDTO) {
        cacheManager.getCache("beerCache").evict(id);
        cacheManager.getCache("beerListCache").clear();
        return beerRepository.findById(id).map(beer -> {
            beer.setBeerName(beerDTO.getBeerName());
            beer.setBeerStyle(beerDTO.getBeerStyle());
            beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            beer.setPrice(beerDTO.getPrice());
            beer.setUpc(beerDTO.getUpc());
            var savedBeer = beerRepository.save(beer);
            var auth = SecurityContextHolder.getContext().getAuthentication();
            applicationEventPublisher.publishEvent(new BeerUpdatedEvent(savedBeer, auth));
            return beerMapper.beerToBeerDTO(savedBeer);
        });
    }

    @Override
    public Optional<BeerDTO> patchBeerById(UUID id, BeerDTO beerDTO) {
        cacheManager.getCache("beerCache").evict(id);
        cacheManager.getCache("beerListCache").clear();
        return beerRepository.findById(id).map(beer -> {
            if (StringUtils.hasText(beerDTO.getBeerName())) {
                beer.setBeerName(beerDTO.getBeerName());
            }
            if (beerDTO.getBeerStyle() != null) {
                beer.setBeerStyle(beerDTO.getBeerStyle());
            }
            if (beerDTO.getQuantityOnHand() != null) {
                beer.setQuantityOnHand(beerDTO.getQuantityOnHand());
            }
            if (beerDTO.getPrice() != null) {
                beer.setPrice(beerDTO.getPrice());
            }
            if (StringUtils.hasText(beerDTO.getUpc())) {
                beer.setUpc(beerDTO.getUpc());
            }
            var savedBeer = beerRepository.save(beer);
            var auth = SecurityContextHolder.getContext().getAuthentication();
            applicationEventPublisher.publishEvent(new BeerPatchedEvent(savedBeer, auth));
            return beerMapper.beerToBeerDTO(savedBeer);
        });
    }

    @Override
    public boolean deleteBeer(UUID id) {
        return beerRepository.findById(id).map(beer -> {
            cacheManager.getCache("beerCache").evict(id);
            cacheManager.getCache("beerListCache").clear();
            beerRepository.delete(beer);
            var auth = SecurityContextHolder.getContext().getAuthentication();
            applicationEventPublisher.publishEvent(new BeerDeletedEvent(beer, auth));
            return true;
        }).orElse(false);
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
        // Set maximum page size constants later 1000
        int queryPageSize = pageSize == null || pageSize <= 0 || pageSize > 1000 ? DEFAULT_PAGE_SIZE : pageSize;

        Sort sort = Sort.by(Sort.Order.desc("beerName"));

        return PageRequest.of(queryPageNumber, queryPageSize, sort);
    }
}
