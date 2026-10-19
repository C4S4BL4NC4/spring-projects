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
public class CustomerRouterConfig {
    public static final String CUSTOMER_PATH = "/api/v0/customers";
    public static final String CUSTOMER_ID_PATH = CUSTOMER_PATH + "/{customerId}";

    private final CustomerHandler customerHandler;

    @Bean
    public RouterFunction<ServerResponse> customerRoutes() {
        return route()
                .GET(CUSTOMER_PATH, accept(APPLICATION_JSON), customerHandler::listCustomers)
                .GET(CUSTOMER_ID_PATH, accept(APPLICATION_JSON), customerHandler::listCustomerById)
                .POST(CUSTOMER_PATH, accept(APPLICATION_JSON), customerHandler::createCustomer)
                .PUT(CUSTOMER_ID_PATH, accept(APPLICATION_JSON), customerHandler::updateCustomerById)
                .PATCH(CUSTOMER_ID_PATH, accept(APPLICATION_JSON), customerHandler::patchCustomerById)
                .DELETE(CUSTOMER_ID_PATH, accept(APPLICATION_JSON), customerHandler::deleteCustomerById)
                .build();
    }
}
