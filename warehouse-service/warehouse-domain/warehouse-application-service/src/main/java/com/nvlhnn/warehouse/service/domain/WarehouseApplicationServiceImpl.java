package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.create.*;
import com.nvlhnn.warehouse.service.domain.dto.response.WarehouseProductStockResponse;
import com.nvlhnn.warehouse.service.domain.ports.input.service.WarehouseApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.UUID;

@Slf4j
@Validated
@Service
public class WarehouseApplicationServiceImpl implements WarehouseApplicationService {

    private final WarehouseCreateCommandHandler warehouseCreateCommandHandler;
    private final WarehouseUpdateCommandHandler warehouseUpdateCommandHandler;
    private final StockCreateCommandHandler stockCreateCommandHandler;
    private final StockTransferCommandHandler stockTransferCommandHandler;
    private final StockUpdateCommandHandler stockUpdateCommandHandler;
    private final WarehouseListCommandHandler warehouseListCommandHandler;
    private final WarehouseGetProductStockCommandHandler warehouseGetProductStockCommandHandler;

    public WarehouseApplicationServiceImpl(WarehouseCreateCommandHandler warehouseCreateCommandHandler,
                                           WarehouseUpdateCommandHandler warehouseUpdateCommandHandler,
                                           StockCreateCommandHandler stockCreateCommandHandler,
                                           WarehouseListCommandHandler warehouseListCommandHandler,
                                           StockTransferCommandHandler stockTransferCommandHandler,
                                           WarehouseGetProductStockCommandHandler warehouseGetProductStockCommandHandler,
                                           StockUpdateCommandHandler stockUpdateCommandHandler) {
        this.warehouseCreateCommandHandler = warehouseCreateCommandHandler;
        this.warehouseUpdateCommandHandler = warehouseUpdateCommandHandler;
        this.stockCreateCommandHandler = stockCreateCommandHandler;
        this.stockTransferCommandHandler = stockTransferCommandHandler;
        this.stockUpdateCommandHandler = stockUpdateCommandHandler;
        this.warehouseGetProductStockCommandHandler = warehouseGetProductStockCommandHandler;
        this.warehouseListCommandHandler = warehouseListCommandHandler;
    }

    @Override
    public CreateWarehouseResponse createWarehouse(CreateWarehouseCommand createWarehouseCommand) {
        log.info("Creating warehouse with name: {}", createWarehouseCommand.getName());
        return warehouseCreateCommandHandler.createWarehouse(createWarehouseCommand);
    }

    @Override
    public CreateWarehouseResponse updateWarehouse(CreateUpdateWarehouseCommand updateWarehouseCommand) {
        log.info("Updating warehouse with id: {}", updateWarehouseCommand.getWarehouseId());
        return warehouseUpdateCommandHandler.updateWarehouse(updateWarehouseCommand);
    }

    @Override
    public WarehouseListResponse listWarehouses(int page, int size) {
        log.info("Listing warehouses for page: {} with size: {}", page, size);
        return warehouseListCommandHandler.listWarehouses(page, size);
    }

    @Override
    public CreateTransferStockResponse transferStock(CreateTransferStockCommand stockTransferCommand) {
        log.info("Transferring stock of product id: {} from warehouse id: {} to warehouse id: {}",
                stockTransferCommand.getProductId(),
                stockTransferCommand.getFromWarehouseId(),
                stockTransferCommand.getToWarehouseId());
        return stockTransferCommandHandler.transferStock(stockTransferCommand);
    }

    @Override
    public CreateStockResponse createStock(CreateStockCommand createStockCommand) {
        log.info("Creating stock for product id: {}", createStockCommand.getProductId());
        return stockCreateCommandHandler.createStock(createStockCommand);
    }

    @Override
    public CreateStockResponse updateStock(CreateUpdateStockCommand updateStockCommand) {
        log.info("Updating stock for product id: {} in warehouse id: {}",
                updateStockCommand.getProductId(), updateStockCommand.getWarehouseId());
        return stockUpdateCommandHandler.updateStock(updateStockCommand);
    }

    @Override
    public WarehouseProductStockResponse getProductStockByWarehouse(UUID warehouseId, UUID productId) {
        log.info("Getting stock for product id: {} in warehouse id: {}", productId, warehouseId);
        return warehouseGetProductStockCommandHandler.getProductStockByWarehouse(warehouseId, productId);
    }
}
