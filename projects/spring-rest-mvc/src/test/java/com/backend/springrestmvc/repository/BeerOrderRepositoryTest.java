package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.entity.Beer;
import com.backend.springrestmvc.entity.BeerOrder;
import com.backend.springrestmvc.entity.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class BeerOrderRepositoryTest {
    @Autowired
    BeerOrderRepository beerOrderRepository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    BeerRepository beerRepository;


    Customer testCustomer;
    Beer testBeer;

    @BeforeEach
    void setUp() {
        testCustomer = customerRepository.findAll().getFirst();
        testBeer = beerRepository.findAll().getFirst();
    }

    @Test
    void testBeerOrders() {
        BeerOrder beerOrder = BeerOrder.builder()
                .customerRef("Test order")
                .customer(testCustomer)
                .build();

        BeerOrder savedBeerOrder = beerOrderRepository.saveAndFlush(beerOrder);
        System.out.println(savedBeerOrder.getCustomerRef());
        System.out.println(savedBeerOrder.getId());
        System.out.println(savedBeerOrder.getCustomer().getId());
    }
}