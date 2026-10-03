package com.backend.springrestmvc.bootstrap;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.entity.Customer;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.repository.BeerRepository;
import com.backend.springrestmvc.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;

    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCustomerData();
    }

    private void loadBeerData() {

        if (beerRepository.count() == 0) {
            var efesMalt = Beer.builder()
                    .beerName("EFES")
                    .beerStyle(BeerStyle.MALT)
                    .upc("222222")
                    .quantityOnHand(110)
                    .price(BigDecimal.valueOf(120.00))
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .updatedAt(LocalDateTime.now().minusMinutes(20))
                    .build();

            var tuborgGold = Beer.builder()
                    .beerName("TUBORG")
                    .beerStyle(BeerStyle.MALT)
                    .upc("111111")
                    .quantityOnHand(60)
                    .price(BigDecimal.valueOf(125.00))
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .updatedAt(LocalDateTime.now().minusMinutes(50))
                    .build();

            var carlsbergPilsner = Beer.builder()
                    .beerName("CARLSBERG")
                    .beerStyle(BeerStyle.PILSNER)
                    .upc("333333")
                    .quantityOnHand(115)
                    .price(BigDecimal.valueOf(120.00))
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .updatedAt(LocalDateTime.now().minusMinutes(20))
                    .build();

            beerRepository.save(efesMalt);
            beerRepository.save(tuborgGold);
            beerRepository.save(carlsbergPilsner);
        }
    }

    private void loadCustomerData() {

        if (customerRepository.count() == 0) {
            var riki = Customer.builder()
                    .name("Riki Maro")
                    .createdDate(LocalDateTime.now().minusDays(30))
                    .lastModifiedDate(LocalDateTime.now().minusHours(30))
                    .build();
            var ibra = Customer.builder()
                    .name("Ibrahim Tatlises")
                    .createdDate(LocalDateTime.now().minusDays(60))
                    .lastModifiedDate(LocalDateTime.now().minusDays(2))
                    .build();

            var carm = Customer.builder()
                    .name("Carmine Berzatto")
                    .createdDate(LocalDateTime.now().minusDays(30))
                    .lastModifiedDate(LocalDateTime.now().minusDays(30))
                    .build();

            customerRepository.save(riki);
            customerRepository.save(ibra);
            customerRepository.save(carm);
        }
    }

}
