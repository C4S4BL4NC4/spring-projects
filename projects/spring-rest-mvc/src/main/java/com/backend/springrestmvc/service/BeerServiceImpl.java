package com.backend.springrestmvc.service;

import com.backend.springrestmvc.model.BeerDTO;
import com.backend.springrestmvc.model.BeerStyle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class BeerServiceImpl implements BeerService {

    private final Map<UUID, BeerDTO> beerMap = new HashMap<>();

    public BeerServiceImpl() {
        var efesMalt = BeerDTO.builder()
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

        var tuborgGold = BeerDTO.builder()
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

        var carlsbergPilsner = BeerDTO.builder()
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
    public List<BeerDTO> getAllBeers() {
        log.debug("getAllBeers() - in beer service");
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("getBeerById() - in beer service, id = {}", id);
        return Optional.ofNullable(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        log.debug("saveNewBeer() - in beer service");
        var now = LocalDateTime.now();
        var newBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .version(0)
                .beerName(beerDTO.getBeerName().toUpperCase())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .price(beerDTO.getPrice())
                .createdAt(now)
                .updatedAt(now)
                .build();
        beerMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }

    @Override
    public Optional<BeerDTO> updateBeer(UUID id, BeerDTO beerDTO) {
        log.debug("updateBeer() - in beer service, id = {}", id);
        var existing = beerMap.get(id);
        if (existing == null) {
            return Optional.empty();
        }
        existing.setBeerName(beerDTO.getBeerName());
        existing.setBeerStyle(beerDTO.getBeerStyle());
        existing.setUpc(beerDTO.getUpc());
        existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existing.setPrice(beerDTO.getPrice());
        existing.setUpdatedAt(LocalDateTime.now());
        return Optional.of(existing);
    }

    @Override
    public void deleteBeer(UUID id) {
        log.debug("deleteBeer() - in beer service, id = {}", id);
        beerMap.remove(id);
    }
}
