package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.create.CreateUpdateStockCommand;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateStockResponse;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockUpdateCommandHandler {

    private final StockUpdateHelper stockUpdateHelper;
    private final WarehouseDataMapper warehouseDataMapper;

    public StockUpdateCommandHandler(StockUpdateHelper stockUpdateHelper, WarehouseDataMapper warehouseDataMapper) {
        this.stockUpdateHelper = stockUpdateHelper;
        this.warehouseDataMapper = warehouseDataMapper;
    }

    public CreateStockResponse updateStock(CreateUpdateStockCommand updateStockCommand) {
        StockUpdatedEvent stockUpdatedEvent = stockUpdateHelper.processStockUpdate(updateStockCommand);

        log.info("Stock update for product ID: {} in warehouse ID: {} completed successfully",
                updateStockCommand.getProductId(), updateStockCommand.getWarehouseId());

        return warehouseDataMapper.stockToCreateStockResponse(stockUpdatedEvent.getStock(),
                "Stock updated successfully");
    }
}
