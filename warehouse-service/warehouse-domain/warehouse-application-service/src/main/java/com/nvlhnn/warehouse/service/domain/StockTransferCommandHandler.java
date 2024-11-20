package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.create.CreateTransferStockCommand;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateTransferStockResponse;
import com.nvlhnn.warehouse.service.domain.event.StockTransferredEvent;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockTransferCommandHandler {

    private final StockTransferHelper stockTransferHelper;
    private final WarehouseDataMapper warehouseDataMapper;

    public StockTransferCommandHandler(StockTransferHelper stockTransferHelper, WarehouseDataMapper warehouseDataMapper) {
        this.stockTransferHelper = stockTransferHelper;
        this.warehouseDataMapper = warehouseDataMapper;
    }

    public CreateTransferStockResponse transferStock(CreateTransferStockCommand transferStockCommand) {
        StockTransferredEvent stockTransferredEvent = stockTransferHelper.processStockTransfer(transferStockCommand);

        log.info("Stock transfer for product ID: {} from warehouse ID: {} to warehouse ID: {} completed successfully",
                transferStockCommand.getProductId(),
                transferStockCommand.getFromWarehouseId(),
                transferStockCommand.getToWarehouseId());

        return warehouseDataMapper.buildStockTransferredResponse(stockTransferredEvent.getFromWarehouseQuantity(),
                stockTransferredEvent.getToWarehouseQuantity(),
                stockTransferredEvent.getProductId(),
                "Stock transfer completed successfully");
    }
}
