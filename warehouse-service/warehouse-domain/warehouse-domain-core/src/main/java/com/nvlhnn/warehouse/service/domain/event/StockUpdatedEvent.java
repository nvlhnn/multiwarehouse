package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Stock;

import java.time.ZonedDateTime;

public class StockUpdatedEvent extends StockEvent{

    private final int quantity;

    private final DomainEventPublisher<StockUpdatedEvent> stockUpdatedEventDomainEventPublisher;

    public StockUpdatedEvent(Stock stock, int quantity, ZonedDateTime createdAt, DomainEventPublisher<StockUpdatedEvent> stockUpdatedEventDomainEventPublisher) {
        super(stock, createdAt);
        this.stockUpdatedEventDomainEventPublisher = stockUpdatedEventDomainEventPublisher;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public void fire(){
        stockUpdatedEventDomainEventPublisher.publish(this);
    }
}
