package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;
import com.nvlhnn.warehouse.service.domain.event.StockTransferredEvent;
import com.nvlhnn.warehouse.service.domain.event.WarehouseCreatedEvent;
import com.nvlhnn.warehouse.service.domain.event.WarehouseUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publish(StockCreatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("StockCreatedEvent published for stock id: {}", domainEvent.getStock().getId().getValue());
    }

    public void publish(StockTransferredEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("StockTransferredEvent published");
    }

    public void publish(WarehouseCreatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("WarehouseCreatedEvent published for warehouse id: {}", domainEvent.getWarehouse().getId().getValue());
    }

    public void publish(WarehouseUpdatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("WarehouseUpdatedEvent published for warehouse id: {}", domainEvent.getWarehouse().getId().getValue());
    }

    public void publish(StockUpdatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("StockUpdatedEvent published for stock id: {}", domainEvent.getStock().getId().getValue());
    }
}
