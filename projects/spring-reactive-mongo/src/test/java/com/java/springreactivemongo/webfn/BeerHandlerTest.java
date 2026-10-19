package com.java.springreactivemongo.webfn;

import com.java.springreactivemongo.model.BeerDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWebTestClient
class BeerHandlerTest {

    @Autowired
    WebTestClient webTestClient;

    @Test
    void listBeers() {
        createBeer();

        webTestClient.get().uri(BeerRouterConfig.BEER_PATH)
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(BeerDTO.class)
                .value(beers -> assertThat(beers).isNotEmpty());
    }

    @Test
    void getBeerById() {
        String beerId = createBeer();

        webTestClient.get().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .value(beer -> {
                    assertThat(beer.getId()).isEqualTo(beerId);
                    assertThat(beer.getBeerName()).isEqualTo("Efes Malt");
                });
    }

    @Test
    void getBeerNotFound() {
        webTestClient.get().uri(BeerRouterConfig.BEER_ID_PATH, "999999")
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void createBeer201() {
        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .bodyValue(getNewBeerDto())
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists("Location");
    }

    @Test
    void createBeerBadData() {
        BeerDTO beer = getNewBeerDto();
        beer.setBeerName("");

        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .bodyValue(beer)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void createBeerMissingPrice() {
        BeerDTO beer = getNewBeerDto();
        beer.setPrice(null);

        webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .bodyValue(beer)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeer() {
        String beerId = createBeer();

        BeerDTO update = getNewBeerDto();
        update.setBeerName("Updated Name");
        update.setQuantityOnHand(5);

        webTestClient.put().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .bodyValue(update)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .value(beer -> {
                    assertThat(beer.getBeerName()).isEqualTo("Updated Name");
                    assertThat(beer.getQuantityOnHand()).isEqualTo(5);
                });
    }

    @Test
    void updateBeerBadData() {
        String beerId = createBeer();

        BeerDTO update = getNewBeerDto();
        update.setUpc("");

        webTestClient.put().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .bodyValue(update)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void updateBeerNotFound() {
        webTestClient.put().uri(BeerRouterConfig.BEER_ID_PATH, "999999")
                .bodyValue(getNewBeerDto())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void patchBeer() {
        String beerId = createBeer();

        BeerDTO patch = BeerDTO.builder()
                .beerName("Patched Name")
                .build();

        webTestClient.patch().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .bodyValue(patch)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .exchange()
                .expectStatus().isOk()
                .expectBody(BeerDTO.class)
                .value(beer -> {
                    assertThat(beer.getBeerName()).isEqualTo("Patched Name");
                    assertThat(beer.getBeerStyle()).isEqualTo("MALT");
                    assertThat(beer.getQuantityOnHand()).isEqualTo(100);
                });
    }

    @Test
    void patchBeerNotFound() {
        webTestClient.patch().uri(BeerRouterConfig.BEER_ID_PATH, "999999")
                .bodyValue(BeerDTO.builder().beerName("Patched Name").build())
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBeer() {
        String beerId = createBeer();

        webTestClient.delete().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .exchange()
                .expectStatus().isNoContent();

        webTestClient.get().uri(BeerRouterConfig.BEER_ID_PATH, beerId)
                .exchange()
                .expectStatus().isNotFound();
    }

    @Test
    void deleteBeerNotFound() {
        webTestClient.delete().uri(BeerRouterConfig.BEER_ID_PATH, "999999")
                .exchange()
                .expectStatus().isNotFound();
    }

    String createBeer() {
        String location = webTestClient.post().uri(BeerRouterConfig.BEER_PATH)
                .bodyValue(getNewBeerDto())
                .exchange()
                .expectStatus().isCreated()
                .returnResult(Void.class)
                .getResponseHeaders()
                .getLocation()
                .toString();

        return location.substring(location.lastIndexOf('/') + 1);
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
