package com.backend.springrestmvc.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.mysql.MySQLContainer;

import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest
@ActiveProfiles("mysql")
public class MySqlIT {
    @Container
    @ServiceConnection
    static MySQLContainer mysql = new MySQLContainer("mysql:9.7");


    @Autowired
    DataSource dataSource;

    
    @Autowired
    BeerRepository beerRepository;


    @Test
    void testListBeers() {
        var beers = beerRepository.findAll();
        assertThat(beers.size()).isGreaterThan(0);
    }
}
