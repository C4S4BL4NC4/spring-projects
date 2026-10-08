package com.backend.springrestmvc.bootstrap;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.entity.Customer;
import com.backend.springrestmvc.model.BeerStyle;
import com.backend.springrestmvc.repository.BeerRepository;
import com.backend.springrestmvc.repository.CustomerRepository;
import com.backend.springrestmvc.service.BeerCsvService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class BootstrapData implements CommandLineRunner {

    private final BeerRepository beerRepository;
    private final CustomerRepository customerRepository;
    private final BeerCsvService beerCsvService;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        loadBeerData();
        loadCsvData();
        loadCustomerData();
    }

    private void loadCsvData() {
        if (beerRepository.count() < 10) {
            File file = new File("./src/main/resources/csvdata/beers.csv");
            var records = beerCsvService.convertCSV(file);

            records.forEach(beerCSVRecord -> {
                BeerStyle beerStyle = switch (beerCSVRecord.getStyle()) {
                    case "American Pale Lager" -> BeerStyle.LAGER;
                    case "American Pale Ale (APA)", "American Black Ale", "Belgian Dark Ale", "American Blonde Ale" ->
                            BeerStyle.ALE;
                    case "American IPA", "American Double / Imperial IPA", "Belgian IPA" -> BeerStyle.IPA;
                    case "American Porter" -> BeerStyle.PORTER;
                    case "Oatmeal Stout", "American Stout" -> BeerStyle.STOUT;
                    case "Saison / Farmhouse Ale" -> BeerStyle.SAISON;
                    case "Fruit / Vegetable Beer", "Winter Warmer", "Berliner Weissbier" -> BeerStyle.WHEAT;
                    case "English Pale Ale" -> BeerStyle.PALE_ALE;
                    default -> BeerStyle.PILSNER;
                };

                beerRepository.save(Beer.builder()
                        .beerName(StringUtils.abbreviate(beerCSVRecord.getBeer(), 50))
                        .beerStyle(beerStyle)
                        .price(BigDecimal.TEN)
                        .upc(beerCSVRecord.getRow().toString())
                        .quantityOnHand(beerCSVRecord.getCount())
                        .build());
            });
        }
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
