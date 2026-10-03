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

    private final Map<UUID, BeerDTO> beerMap;

    public BeerServiceImpl() {
        this.beerMap = new HashMap<>();

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
    public void updateBeer(UUID id, BeerDTO beerDTO) {
        log.debug("BeerController updateBeer beerId = " + id);
        var existing = this.beerMap.get(id);
        // No checking
        existing.setBeerName(beerDTO.getBeerName());
        existing.setBeerStyle(beerDTO.getBeerStyle());
        existing.setUpc(beerDTO.getUpc());
        existing.setQuantityOnHand(beerDTO.getQuantityOnHand());
        existing.setPrice(beerDTO.getPrice());
        existing.setUpdatedAt(LocalDateTime.now());
        this.beerMap.put(existing.getId(), existing);
    }

    @Override
    public List<BeerDTO> getAllBeers() {
        log.debug("getAllBeers() - in beer service");
        return new ArrayList<>(beerMap.values());
    }

    @Override
    public Optional<BeerDTO> getBeerById(UUID id) {
        log.debug("getBeerById() - in beer service");
        return Optional.of(beerMap.get(id));
    }

    @Override
    public BeerDTO saveNewBeer(BeerDTO beerDTO) {
        log.debug("saveNewBeer() - in beerDTO service");
        var newBeer = BeerDTO.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .version(0)
                .beerName(beerDTO.getBeerName().toUpperCase())
                .beerStyle(beerDTO.getBeerStyle())
                .upc(beerDTO.getUpc())
                .price(beerDTO.getPrice())
                .quantityOnHand(beerDTO.getQuantityOnHand())
                .build();
        beerMap.put(newBeer.getId(), newBeer);
        return newBeer;
    }

    @Override
    public void deleteBeer(UUID beerId) {
        // Skip checking
        this.beerMap.remove(beerId);
    }
}
