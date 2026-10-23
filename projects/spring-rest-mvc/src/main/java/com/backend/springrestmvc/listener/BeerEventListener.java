package com.backend.springrestmvc.listener;

import com.backend.springrestmvc.event.BeerCreatedEvent;
import com.backend.springrestmvc.event.BeerDeletedEvent;
import com.backend.springrestmvc.event.BeerEvent;
import com.backend.springrestmvc.event.BeerPatchedEvent;
import com.backend.springrestmvc.event.BeerUpdatedEvent;
import com.backend.springrestmvc.mapper.BeerMapper;
import com.backend.springrestmvc.repository.BeerAuditRepsitory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class BeerEventListener {

    private final BeerMapper beerMapper;

    private final BeerAuditRepsitory beerAuditRepsitory;

    @Async
    @EventListener
    public void listen(BeerEvent event) {
        var beerAudit = beerMapper.beerToBeerAudit(event.getBeer());

        String eventType = switch (event) {
            case BeerCreatedEvent e -> "BEER_CREATED";
            case BeerUpdatedEvent e -> "BEER_PUT";
            case BeerPatchedEvent e -> "BEER_PATCH";
            case BeerDeletedEvent e -> "BEER_DELETE";
            default -> "UNKNOWN";
        };
        beerAudit.setAuditEventType(eventType);

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        var savedBeerAudit = beerAuditRepsitory.save(beerAudit);
        log.debug("Beer Audit Created: {} - {}", eventType, savedBeerAudit.getId());
    }
}
