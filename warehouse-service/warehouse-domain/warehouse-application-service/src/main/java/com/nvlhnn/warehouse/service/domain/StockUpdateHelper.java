package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateUpdateStockCommand;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class StockUpdateHelper {

    private final WarehouseDomainService warehouseDomainService;
    private final StockRepository stockRepository;
    private final WarehouseDataMapper warehouseDataMapper;
    private final StockUpdatedEventPublisher stockUpdatedEventPublisher;

    public StockUpdateHelper(WarehouseDomainService warehouseDomainService,
                             StockRepository stockRepository,
                             WarehouseDataMapper warehouseDataMapper,
                             StockUpdatedEventPublisher stockUpdatedEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.stockRepository = stockRepository;
        this.warehouseDataMapper = warehouseDataMapper;
        this.stockUpdatedEventPublisher = stockUpdatedEventPublisher;
    }

    @Transactional
    public StockUpdatedEvent processStockUpdate(CreateUpdateStockCommand updateStockCommand) {
        Stock stock = getStock(updateStockCommand.getProductId(), updateStockCommand.getWarehouseId());

        StockUpdatedEvent stockUpdatedEvent = warehouseDomainService.updateStock(stock, updateStockCommand.getQuantity(),
                stockUpdatedEventPublisher);

        saveStock(stock);
        return stockUpdatedEvent;
    }

    private Stock getStock(UUID productId, UUID warehouseId) {
        return stockRepository.findByWarehouseIdAndProductId(new WarehouseId(warehouseId), new ProductId(productId))
                .orElseThrow(() -> {
                    log.warn("Stock not found for product id: {} in warehouse id: {}", productId, warehouseId);
                    return new WarehouseDomainException("Stock not found.");
                });
    }

    private void saveStock(Stock stock) {
        Stock savedStock = stockRepository.save(stock);
        if (savedStock == null) {
            log.error("Could not save stock with product id: {} in warehouse id: {}", stock.getProductId().getValue(), stock.getWarehouseId().getValue());
            throw new WarehouseDomainException("Failed to save updated stock.");
        }
        log.info("Stock is saved with id: {}", savedStock.getId().getValue());
    }
}
