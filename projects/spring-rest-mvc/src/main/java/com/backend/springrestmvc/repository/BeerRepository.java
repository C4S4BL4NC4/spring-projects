package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.entity.Beer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface BeerRepository extends JpaRepository<Beer, UUID> {
    
}
