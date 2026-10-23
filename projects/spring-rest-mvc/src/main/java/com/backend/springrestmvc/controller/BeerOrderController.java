package com.backend.springrestmvc.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RequiredArgsConstructor
@RestController
public class BeerOrderController {
    public static final String ORDERS_PATH = "/api/mvc/orders";
    public static final String ORDER_PATH_ID = ORDERS_PATH + "/{orderId}";


}
