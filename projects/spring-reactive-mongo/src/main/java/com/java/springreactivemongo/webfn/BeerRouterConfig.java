package com.java.springreactivemongo.webfn;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
@RequiredArgsConstructor
public class BeerRouterConfig {
    public static final String BEER_PATH = "/api/reactive-mongo/beers";
    public static final String BEER_ID_PATH = BEER_PATH + "/{beerId}";

    private final BeerHandler beerHandler;

    @Bean

    public RouterFunction<ServerResponse> routes() {
        return route()
                .GET(BEER_PATH, accept(APPLICATION_JSON), beerHandler::listBeers)
                .GET(BEER_ID_PATH, accept(APPLICATION_JSON), beerHandler::listBeerById)
                .POST(BEER_PATH, accept(APPLICATION_JSON), beerHandler::createBeer)
                .PUT(BEER_ID_PATH, accept(APPLICATION_JSON), beerHandler::updateBeerById)
                .PATCH(BEER_ID_PATH, accept(APPLICATION_JSON), beerHandler::patchBeerById)
                .DELETE(BEER_ID_PATH, accept(APPLICATION_JSON), beerHandler::deleteBeerById)
                .build();
    }
}
