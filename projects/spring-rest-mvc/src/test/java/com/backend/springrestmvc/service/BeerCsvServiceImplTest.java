package com.backend.springrestmvc.service;

import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

class BeerCsvServiceImplTest {

    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCSV() {
        File csvFile = new File("./src/main/resources/csvdata/beers.csv");
        var records = beerCsvService.convertCSV(csvFile);

        System.out.println(records.size());
        assertThat(records.size()).isGreaterThan(0);
    }
}