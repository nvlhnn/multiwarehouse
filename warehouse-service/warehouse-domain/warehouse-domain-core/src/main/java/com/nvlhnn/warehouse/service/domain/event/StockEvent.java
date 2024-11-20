package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.DomainEvent;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;
import java.util.Optional;

public abstract class StockEvent implements DomainEvent<Stock> {

    private final Stock stock;

    private final ZonedDateTime createdAt;

    public StockEvent(Stock stock, ZonedDateTime createdAt){
        this.stock = stock;
        this.createdAt = createdAt;
    }

    public Stock getStock() {
        return stock;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }
}
