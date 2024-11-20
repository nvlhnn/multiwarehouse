package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;

public class WarehouseUpdatedEvent extends WarehouseEvent{

    private final DomainEventPublisher<WarehouseUpdatedEvent> warehouseUpdatedEventDomainEventPublisher;

    public WarehouseUpdatedEvent(Warehouse warehouse, ZonedDateTime createdAt, DomainEventPublisher<WarehouseUpdatedEvent> warehouseUpdatedEventDomainEventPublisher) {
        super(warehouse, createdAt);
        this.warehouseUpdatedEventDomainEventPublisher = warehouseUpdatedEventDomainEventPublisher;
    }

    @Override
    public void fire(){
        warehouseUpdatedEventDomainEventPublisher.publish(this);
    }
}
