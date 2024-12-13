package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateUpdateStockCommand;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;
import com.nvlhnn.warehouse.service.domain.event.StockEvent;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockCreatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.ProductRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.WarehouseRepository;
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
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final StockCreatedEventPublisher stockCreatedEventPublisher;

    public StockUpdateHelper(WarehouseDomainService warehouseDomainService,
                             StockRepository stockRepository,
                             WarehouseRepository warehouseRepository,
                             ProductRepository productRepository,
                             WarehouseDataMapper warehouseDataMapper,
                             StockCreatedEventPublisher stockCreatedEventPublisher,
                             StockUpdatedEventPublisher stockUpdatedEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.warehouseDataMapper = warehouseDataMapper;
        this.stockCreatedEventPublisher = stockCreatedEventPublisher;
        this.stockUpdatedEventPublisher = stockUpdatedEventPublisher;
    }

    @Transactional
    public StockEvent processStockUpdate(CreateUpdateStockCommand updateStockCommand) {

        // Validate warehouse and product existence
        Optional<Warehouse> warehouse = warehouseRepository.findById(new WarehouseId(updateStockCommand.getWarehouseId()));
        if (!warehouse.isPresent()) {
            log.warn("Warehouse not found with id: {}", updateStockCommand.getWarehouseId());
            throw new WarehouseDomainException("Warehouse not found.");
        }

        Optional<Product> product = productRepository.findById(new ProductId(updateStockCommand.getProductId()));
        if (!product.isPresent()) {
            log.warn("Product not found with id: {}", updateStockCommand.getProductId());
            throw new WarehouseDomainException("Product not found.");
        }

        // Check if stock exists (findByWarehouseIdAndProductId)
        Optional<Stock> existingStock = stockRepository.findByWarehouseIdAndProductId(
                new WarehouseId(updateStockCommand.getWarehouseId()),
                new ProductId(updateStockCommand.getProductId())
        );

        if (!existingStock.isPresent()) {
            // Create new stock if it doesn't exist
            Stock stock = warehouseDataMapper.createStockFromCreateUpdateStockCommand(updateStockCommand);
            StockCreatedEvent stockCreatedEvent = warehouseDomainService.createStock(stock, warehouse.get(), product.get(), stockCreatedEventPublisher);
            saveStock(stock);

            Integer productTotalQuantity = stockRepository.getProductTotalQuantity(new ProductId(updateStockCommand.getProductId()));
            stockCreatedEvent.setProductTotalQuantity(productTotalQuantity);

            return stockCreatedEvent;  // Return the created event
        } else {
            // Update stock if it exists
            StockUpdatedEvent stockUpdatedEvent = warehouseDomainService.updateStock(
                    existingStock.get(),updateStockCommand.getQuantity(), stockUpdatedEventPublisher
            );
            saveStock(existingStock.get());

            Integer productTotalQuantity = stockRepository.getProductTotalQuantity(new ProductId(updateStockCommand.getProductId()));
            productTotalQuantity = productTotalQuantity != null ? productTotalQuantity : 0;
            stockUpdatedEvent.setProductTotalQuantity(productTotalQuantity);

            return stockUpdatedEvent;  // Return the updated event
        }
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
