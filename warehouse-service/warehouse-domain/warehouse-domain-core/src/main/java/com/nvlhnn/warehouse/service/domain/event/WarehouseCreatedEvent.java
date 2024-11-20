package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;

public class WarehouseCreatedEvent extends WarehouseEvent{

    private final DomainEventPublisher<WarehouseCreatedEvent> warehouseCreatedEventDomainEventPublisher;

    public WarehouseCreatedEvent(Warehouse warehouse, ZonedDateTime createdAt, DomainEventPublisher<WarehouseCreatedEvent> warehouseCreatedEventDomainEventPublisher) {
        super(warehouse, createdAt);
        this.warehouseCreatedEventDomainEventPublisher = warehouseCreatedEventDomainEventPublisher;
    }

    @Override
    public void fire(){
        warehouseCreatedEventDomainEventPublisher.publish(this);
    }
}
