package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.Beer;
import com.backend.springrestmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, Beer> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

        var efesMalt = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("EFES")
                .beerStyle(BeerStyle.MALT)
                .upc("222222")
                .quantityOnHand(110)
                .price(BigDecimal.valueOf(120.00))
                .createdAt(LocalDateTime.now().minusDays(3))
                .updatedAt(LocalDateTime.now().minusMinutes(20))
                .build();

        var tuborgGold = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("TUBORG")
                .beerStyle(BeerStyle.MALT)
                .upc("111111")
                .quantityOnHand(60)
                .price(BigDecimal.valueOf(125.00))
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now().minusMinutes(50))
                .build();

        var carlsbergPilsner = Beer.builder()
                .id(UUID.randomUUID())
                .version(1)
                .beerName("CARLSBERG")
                .beerStyle(BeerStyle.PILSNER)
                .upc("333333")
                .quantityOnHand(115)
                .price(BigDecimal.valueOf(120.00))
                .createdAt(LocalDateTime.now().minusDays(1))
                .updatedAt(LocalDateTime.now().minusMinutes(20))
                .build();

        beerMap.put(efesMalt.getId(), efesMalt);
        beerMap.put(tuborgGold.getId(), tuborgGold);
        beerMap.put(carlsbergPilsner.getId(), carlsbergPilsner);
    }

    @Override
    public List<Beer> getAllBeers() {
        log.debug("getAllBeers() - in beer service");
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Beer getBeerById(UUID id) {
        log.debug("getBeerById() - in beer service");
        return beerMap.get(id);
    }

    @Override
    public Beer saveNewBeer(Beer beer) {
        log.debug("saveNewBeer() - in beer service");
        var newBeer = Beer.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0)
                .beerName(beer.getBeerName().toUpperCase())
                .beerStyle(beer.getBeerStyle())
                .upc(beer.getUpc())
                .price(beer.getPrice())
                .quantityOnHand(beer.getQuantityOnHand())
                .build();
        beerMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }
}
