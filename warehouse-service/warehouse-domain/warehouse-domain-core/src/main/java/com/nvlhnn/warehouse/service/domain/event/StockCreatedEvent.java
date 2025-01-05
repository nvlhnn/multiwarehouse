package com.nvlhnn.warehouse.service.domain.event;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.entity.Stock;

import java.time.ZonedDateTime;

public class StockCreatedEvent extends StockEvent{


    private final DomainEventPublisher<StockCreatedEvent> stockCreatedEventDomainEventPublisher;
    private final String warehouseName;
    private final Double warehouseLatitude;
    private final Double warehouseLongitude;
    private final String productName;
    private Integer productTotalQuantity;


    public StockCreatedEvent(Stock stock,
                             String warehouseName,
                             Double warehouseLatitude,
                             Double warehouseLongitude,
                             String productName,
                             ZonedDateTime createdAt, DomainEventPublisher<StockCreatedEvent> stockCreatedEventDomainEventPublisher) {
        super(stock, createdAt);
        this.warehouseName = warehouseName;
        this.warehouseLatitude = warehouseLatitude;
        this.warehouseLongitude = warehouseLongitude;
        this.productName = productName;
        this.stockCreatedEventDomainEventPublisher = stockCreatedEventDomainEventPublisher;
    }

    public Integer getProductTotalQuantity() {
        return productTotalQuantity;
    }

    public void setProductTotalQuantity(Integer productTotalQuantity) {
        this.productTotalQuantity = productTotalQuantity;
    }

    public String getWarehouseName() {
        return warehouseName;
    }

    public Double getWarehouseLatitude() {
        return warehouseLatitude;
    }

    public Double getWarehouseLongitude() {
        return warehouseLongitude;
    }

    public String getProductName() {
        return productName;
    }

    @Override
    public void fire(){
        stockCreatedEventDomainEventPublisher.publish(this);
    }
}
