package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;

public class StockCreatedEvent extends StockEvent{


    private final DomainEventPublisher<StockCreatedEvent> stockCreatedEventDomainEventPublisher;

    public StockCreatedEvent(Stock stock, ZonedDateTime createdAt, DomainEventPublisher<StockCreatedEvent> stockCreatedEventDomainEventPublisher) {
        super(stock, createdAt);
        this.stockCreatedEventDomainEventPublisher = stockCreatedEventDomainEventPublisher;
    }

    @Override
    public void fire(){
        stockCreatedEventDomainEventPublisher.publish(this);
    }
}
