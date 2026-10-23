package com.backend.springrestmvc.listener;

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
public class BeerUpdatedListener {

    private final BeerMapper beerMapper;

    private final BeerAuditRepsitory beerAuditRepsitory;

    @Async
    @EventListener
    public void listen(BeerUpdatedEvent event) {
        var beerAudit = beerMapper.beerToBeerAudit(event.getBeer());
        beerAudit.setAuditEventType("BEER_PUT");

        if (event.getAuthentication() != null && event.getAuthentication().getName() != null) {
            beerAudit.setPrincipalName(event.getAuthentication().getName());
        }

        var savedBeerAudit = beerAuditRepsitory.save(beerAudit);
        log.debug("Beer Audit Updated: {}", savedBeerAudit.getId());
    }
}
