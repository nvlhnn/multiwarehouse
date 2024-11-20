package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.create.CreateStockCommand;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateStockResponse;
import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockCreatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockCreateCommandHandler {

    private final StockCreateHelper stockCreateHelper;
    private final WarehouseDataMapper warehouseDataMapper;
    private final StockCreatedEventPublisher stockCreatedEventPublisher;

    public StockCreateCommandHandler(StockCreateHelper stockCreateHelper,
                                     WarehouseDataMapper warehouseDataMapper,
                                     StockCreatedEventPublisher stockCreatedEventPublisher) {
        this.stockCreateHelper = stockCreateHelper;
        this.warehouseDataMapper = warehouseDataMapper;
        this.stockCreatedEventPublisher = stockCreatedEventPublisher;
    }

    public CreateStockResponse createStock(CreateStockCommand createStockCommand) {
        StockCreatedEvent stockCreatedEvent = stockCreateHelper.processStockCreation(createStockCommand);
        log.info("Stock created with id: {} for product id: {}", stockCreatedEvent.getStock().getId().getValue(), stockCreatedEvent.getStock().getProductId());
        stockCreatedEventPublisher.publish(stockCreatedEvent);
        return warehouseDataMapper.stockToCreateStockResponse(stockCreatedEvent.getStock(), "Stock created successfully");
    }
}
