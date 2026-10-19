package com.java.springreactivemongo.webfn;

import com.java.springreactivemongo.model.BeerDTO;
import com.java.springreactivemongo.service.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BeerHandler {
    private final BeerService beerService;
    private final Validator validator;

    private void validate(BeerDTO beerDTO) {
        Errors errors = new BeanPropertyBindingResult(beerDTO, "beerDto");
        validator.validate(beerDTO, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    private <T> Mono<T> notFound() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Mono<ServerResponse> listBeers(ServerRequest request) {
        return ServerResponse.ok().body(beerService.listBeers(), BeerDTO.class);
    }

    public Mono<ServerResponse> listBeerById(ServerRequest request) {
        return ServerResponse.ok().body(beerService.findById(request.pathVariable("beerId"))
                .switchIfEmpty(notFound()), BeerDTO.class);
    }

    public Mono<ServerResponse> createBeer(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beerService::saveBeer)
                .flatMap(beerDTO -> ServerResponse.created(UriComponentsBuilder
                                .fromPath(BeerRouterConfig.BEER_ID_PATH)
                                .build(beerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> updateBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .doOnNext(this::validate)
                .flatMap(beerDTO -> beerService.updateBeer(request.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(notFound())
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchBeerById(ServerRequest request) {
        return request.bodyToMono(BeerDTO.class)
                .flatMap(beerDTO -> beerService.patchBeer(request.pathVariable("beerId"), beerDTO))
                .switchIfEmpty(notFound())
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteBeerById(ServerRequest request) {
        return beerService.findById(request.pathVariable("beerId"))
                .switchIfEmpty(notFound())
                .flatMap(beerDTO -> beerService.deleteBeerById(beerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }
}
