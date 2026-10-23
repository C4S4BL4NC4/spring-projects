package com.backend.springrestmvc.event;

import com.backend.springrestmvc.entity.Beer;
import org.springframework.security.core.Authentication;

public interface BeerEvent {
    Beer getBeer();

    Authentication getAuthentication();
}
