package com.backend.springrestmvc.repository;

import com.backend.springrestmvc.entity.BeerAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BeerAuditRepsitory extends JpaRepository<BeerAudit, UUID> {

}
