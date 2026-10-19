package com.backend.springreactive.controller;

import com.backend.springreactive.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class BeerControllerTest {

    private static final String BEER_URL = "/api/v2/beers";
    private static final String BEER_URL_ID = BEER_URL + "/{beerId}";

    @Autowired
    WebTestClient webTestClient;

    @Test
    void getBeerNotFound() {
        webTestClient.get().uri(BEER_URL_ID, 999999)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createBeerBadData() {
        BeerDTO beer = getNewBeerDto();
        beer.setBeerName("");

        webTestClient.post().uri(BEER_URL)
                .bodyValue(beer)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createBeerMissingPrice() {
        BeerDTO beer = getNewBeerDto();
        beer.setPrice(null);

        webTestClient.post().uri(BEER_URL)
                .bodyValue(beer)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeerBadData() {
        BeerDTO savedBeer = createBeer();

        BeerDTO update = getNewBeerDto();
        update.setUpc("");

        webTestClient.put().uri(BEER_URL_ID, savedBeer.getId())
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeer() {
        BeerDTO savedBeer = createBeer();

        BeerDTO update = getNewBeerDto();
        update.setBeerName("Updated Name");
        update.setQuantityOnHand(5);

        webTestClient.put().uri(BEER_URL_ID, savedBeer.getId())
                .bodyValue(update)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .value(beer -> {
                    assertThat(beer.getId()).isEqualTo(savedBeer.getId());
                    assertThat(beer.getBeerName()).isEqualTo("Updated Name");
                    assertThat(beer.getQuantityOnHand()).isEqualTo(5);
                });

        webTestClient.get().uri(BEER_URL_ID, savedBeer.getId())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .value(beer -> assertThat(beer.getBeerName()).isEqualTo("Updated Name"));
    }

    @Test
    void updateBeerNotFound() {
        webTestClient.put().uri(BEER_URL_ID, 999999)
                .bodyValue(getNewBeerDto())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchBeer() {
        BeerDTO savedBeer = createBeer();

        BeerDTO patch = BeerDTO.builder()
                .beerName("Patched Name")
                .build();

        webTestClient.patch().uri(BEER_URL_ID, savedBeer.getId())
                .bodyValue(patch)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .value(beer -> {
                    assertThat(beer.getBeerName()).isEqualTo("Patched Name");
                    assertThat(beer.getBeerStyle()).isEqualTo(savedBeer.getBeerStyle());
                    assertThat(beer.getUpc()).isEqualTo(savedBeer.getUpc());
                    assertThat(beer.getQuantityOnHand()).isEqualTo(savedBeer.getQuantityOnHand());
                    assertThat(beer.getPrice()).isEqualByComparingTo(savedBeer.getPrice());
                });
    }

    @Test
    void patchBeerNotFound() {
        webTestClient.patch().uri(BEER_URL_ID, 999999)
                .bodyValue(BeerDTO.builder().beerName("Patched Name").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBeer() {
        BeerDTO savedBeer = createBeer();

        webTestClient.delete().uri(BEER_URL_ID, savedBeer.getId())
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(BEER_URL_ID, savedBeer.getId())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBeerNotFound() {
        webTestClient.delete().uri(BEER_URL_ID, 999999)
                .exchange()
                .expectStatus().isNotFound();
    }

    BeerDTO createBeer() {
        BeerDTO savedBeer = webTestClient.post().uri(BEER_URL)
                .bodyValue(getNewBeerDto())
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .returnResult()
                .getResponseBody();

        assertThat(savedBeer).isNotNull();
        assertThat(savedBeer.getId()).isNotNull();
        return savedBeer;
    }

    BeerDTO getNewBeerDto() {
        return BeerDTO.builder()
                .beerName("Efes Malt")
                .beerStyle("MALT")
                .price(new BigDecimal("130.00"))
                .upc("2342134")
                .quantityOnHand(100)
                .build();
    }
}
