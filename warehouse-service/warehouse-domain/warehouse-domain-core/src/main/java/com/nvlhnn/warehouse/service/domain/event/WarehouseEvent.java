package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.DomainEvent;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;

public abstract class WarehouseEvent implements DomainEvent<Warehouse> {

    private final Warehouse warehouse;

    private final ZonedDateTime createdAt;

    public WarehouseEvent(Warehouse warehouse, ZonedDateTime createdAt){
        this.warehouse = warehouse;
        this.createdAt = createdAt;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
