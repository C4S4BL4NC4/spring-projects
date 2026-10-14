package com.backend.springresttemplates.client;

import com.backend.springresttemplates.model.BeerDTO;
import org.springframework.data.domain.Page;

public interface BeerClient {
    Page<BeerDTO> listBeers();
}
