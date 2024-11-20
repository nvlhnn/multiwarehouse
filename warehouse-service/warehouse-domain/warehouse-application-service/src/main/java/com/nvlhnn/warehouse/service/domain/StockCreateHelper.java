package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateStockCommand;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockCreatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.ProductRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Component
public class StockCreateHelper {

    private final WarehouseDomainService warehouseDomainService;
    private final StockRepository stockRepository;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseDataMapper warehouseDataMapper;
    private final StockCreatedEventPublisher stockCreatedEventPublisher;
    private final ProductRepository productRepository;

    public StockCreateHelper(WarehouseDomainService warehouseDomainService,
                             StockRepository stockRepository,
                             WarehouseRepository warehouseRepository,
                             ProductRepository productRepository,
                             WarehouseDataMapper warehouseDataMapper,
                             StockCreatedEventPublisher stockCreatedEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
        this.warehouseDataMapper = warehouseDataMapper;
        this.productRepository = productRepository;
        this.stockCreatedEventPublisher = stockCreatedEventPublisher;
    }

    @Transactional
    public StockCreatedEvent processStockCreation(CreateStockCommand createStockCommand) {
        Warehouse warehouse = getWarehouseById(new WarehouseId(createStockCommand.getWarehouseId()));
        Product product = getProductById(new ProductId(createStockCommand.getProductId()));

        Stock stock = warehouseDataMapper.createStockfromCreateStockCommand(createStockCommand);
        validateStockDoesNotExist(stock);

        StockCreatedEvent stockCreatedEvent = warehouseDomainService.createStock(stock, warehouse, product, stockCreatedEventPublisher);
        saveStock(stock);

        return stockCreatedEvent;
    }

    private Warehouse getWarehouseById(WarehouseId warehouseId) {
        return warehouseRepository.findById(warehouseId)
                .orElseThrow(() -> new WarehouseDomainException("Warehouse not found with id: " + warehouseId.getValue().toString()));
    }

    private Product getProductById(ProductId productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new WarehouseDomainException("Product not found with id: " + productId.getValue().toString()));
    }

    private void validateStockDoesNotExist(Stock stock) {
        Optional<Stock> existingStock = stockRepository.findByWarehouseIdAndProductId(stock.getWarehouseId(), stock.getProductId());
        if (existingStock.isPresent()) {
            log.warn("Stock already exists for product id: {} in warehouse id: {}", stock.getProductId().getValue(), stock.getWarehouseId().getValue());
            throw new WarehouseDomainException("Stock already exists for this product in the specified warehouse.");
        }
    }

    private void saveStock(Stock stock) {
        Stock savedStock = stockRepository.save(stock);
        if (savedStock == null) {
            log.error("Could not save stock with product id: {} in warehouse id: {}", stock.getProductId().getValue(), stock.getWarehouseId().getValue());
            throw new WarehouseDomainException("Failed to save stock.");
        }
        log.info("Stock is saved with id: {}", savedStock.getId().getValue());
    }
}
