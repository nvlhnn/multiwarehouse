package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.DomainEvent;
import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;
import java.util.UUID;

public class StockTransferredEvent extends StockEvent{

    private final DomainEventPublisher<StockTransferredEvent> stockTransferredEventDomainEventPublisher;

    private final int fromWarehouseQuantity;
    private final int toWarehouseQuantity;
    private final UUID productId;

    public StockTransferredEvent(Stock stock, int fromWarehouseQuantity, int toWarehouseQuantity, UUID productId, ZonedDateTime createdAt, DomainEventPublisher<StockTransferredEvent> stockTransferredEventDomainEventPublisher) {
        super(stock, createdAt);
        this.stockTransferredEventDomainEventPublisher = stockTransferredEventDomainEventPublisher;
        this.fromWarehouseQuantity = fromWarehouseQuantity;
        this.toWarehouseQuantity = toWarehouseQuantity;
        this.productId = productId;
    }

    public DomainEventPublisher<StockTransferredEvent> getStockTransferredEventDomainEventPublisher() {
        return stockTransferredEventDomainEventPublisher;
    }

    public int getFromWarehouseQuantity() {
        return fromWarehouseQuantity;
    }

    public int getToWarehouseQuantity() {
        return toWarehouseQuantity;
    }

    public UUID getProductId() {
        return productId;
    }

    @Override
    public void fire(){
        stockTransferredEventDomainEventPublisher.publish(this);
    }

}
