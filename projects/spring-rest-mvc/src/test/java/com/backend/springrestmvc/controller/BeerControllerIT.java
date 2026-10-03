package com.backend.springrestmvc.controller;

import com.backend.springrestmvc.repository.BeerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.core.support.RepositoryMethodInvocationListener;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BeerControllerIT {
    @Autowired
    BeerController beerController;

    @Autowired
    BeerRepository beerRepository;
    private RepositoryMethodInvocationListener repositoryMethodInvocationListener;

    @Test
    void getAllBeers() {
        var dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(3);
    }

    @Rollback
    @Transactional
    @Test
    void emptyList() {
        beerRepository.deleteAll();
        var dtos = beerController.getAllBeers();
        assertThat(dtos.size()).isEqualTo(0);

    }
}