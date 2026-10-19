package com.java.springreactivemongo.webfn;

import com.java.springreactivemongo.model.CustomerDTO;
import com.java.springreactivemongo.service.CustomerService;
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
public class CustomerHandler {
    private final CustomerService customerService;
    private final Validator validator;

    private void validate(CustomerDTO customerDTO) {
        Errors errors = new BeanPropertyBindingResult(customerDTO, "customerDto");
        validator.validate(customerDTO, errors);

        if (errors.hasErrors()) {
            throw new ServerWebInputException(errors.toString());
        }
    }

    private <T> Mono<T> notFound() {
        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Mono<ServerResponse> listCustomers(ServerRequest request) {
        return ServerResponse.ok().body(customerService.listCustomers(), CustomerDTO.class);
    }

    public Mono<ServerResponse> listCustomerById(ServerRequest request) {
        return ServerResponse.ok().body(customerService.findById(request.pathVariable("customerId"))
                .switchIfEmpty(notFound()), CustomerDTO.class);
    }

    public Mono<ServerResponse> createCustomer(ServerRequest request) {
        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerService::saveCustomer)
                .flatMap(customerDTO -> ServerResponse.created(UriComponentsBuilder
                                .fromPath(CustomerRouterConfig.CUSTOMER_ID_PATH)
                                .build(customerDTO.getId()))
                        .build());
    }

    public Mono<ServerResponse> updateCustomerById(ServerRequest request) {
        return request.bodyToMono(CustomerDTO.class)
                .doOnNext(this::validate)
                .flatMap(customerDTO -> customerService.updateCustomer(request.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(notFound())
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> patchCustomerById(ServerRequest request) {
        return request.bodyToMono(CustomerDTO.class)
                .flatMap(customerDTO -> customerService.patchCustomer(request.pathVariable("customerId"), customerDTO))
                .switchIfEmpty(notFound())
                .flatMap(savedDto -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> deleteCustomerById(ServerRequest request) {
        return customerService.findById(request.pathVariable("customerId"))
                .switchIfEmpty(notFound())
                .flatMap(customerDTO -> customerService.deleteCustomerById(customerDTO.getId()))
                .then(ServerResponse.noContent().build());
    }
}
