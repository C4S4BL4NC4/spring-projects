package com.backend.springrestmvc.event;

import com.backend.springrestmvc.entity.Beer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;

@Setter
@Getter
@AllArgsConstructor
@Builder
public class BeerDeletedEvent implements BeerEvent {
    private Beer beer;
    private Authentication authentication;
}
