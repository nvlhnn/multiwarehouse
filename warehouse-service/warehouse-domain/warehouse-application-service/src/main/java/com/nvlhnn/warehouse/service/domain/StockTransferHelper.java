package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateTransferStockCommand;
import com.nvlhnn.warehouse.service.domain.entity.Product;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.StockTransferredEvent;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockTransferredEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.ProductRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.WarehouseRepository;
import com.nvlhnn.warehouse.service.domain.valueobject.StockTransfer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class StockTransferHelper {

    private final WarehouseDomainService warehouseDomainService;
    private final StockRepository stockRepository;
    private final WarehouseRepository warehouseRepository;
    private final ProductRepository productRepository;
    private final StockTransferredEventPublisher stockTransferredEventPublisher;

    public StockTransferHelper(WarehouseDomainService warehouseDomainService,
                               StockRepository stockRepository,
                               WarehouseRepository warehouseRepository,
                               ProductRepository productRepository,
                               StockTransferredEventPublisher stockTransferredEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.stockRepository = stockRepository;
        this.warehouseRepository = warehouseRepository;
        this.productRepository = productRepository;
        this.stockTransferredEventPublisher = stockTransferredEventPublisher;
    }

    @Transactional
    public StockTransferredEvent processStockTransfer(CreateTransferStockCommand transferStockCommand) {
        Warehouse fromWarehouse = getWarehouse(transferStockCommand.getFromWarehouseId());
        Warehouse toWarehouse = getWarehouse(transferStockCommand.getToWarehouseId());
        Product product = getProductById(transferStockCommand.getProductId());

        Optional<Stock> fromStock = getStock(product.getId().getValue(), fromWarehouse.getId().getValue());
        Optional<Stock>  toStock = getStock(product.getId().getValue(), toWarehouse.getId().getValue());

        if(!fromStock.isPresent()){
            log.warn("Stock not found with warehouse id: {} and product id: {}", fromWarehouse.getId().getValue(), toWarehouse.getId().getValue());
            throw new WarehouseDomainException("Stock not found.");
        }

        StockTransferredEvent stockTransferredEvent = warehouseDomainService.transferStock(
                fromWarehouse, toWarehouse, fromStock.get(), toStock, product, transferStockCommand.getQuantity(), stockTransferredEventPublisher);

        saveStock(fromStock.get());

        if (toWarehouse.getStocks().isEmpty()){
            saveStock(toStock.get());
        }else{
            saveStock(toWarehouse.getStocks().get(0));
        }


//        saveStockStockTransfer(fromStock.get().getStockTransfers().get(0));

        return stockTransferredEvent;
    }

    private Warehouse getWarehouse(UUID warehouseId) {
        return warehouseRepository.findById(new WarehouseId(warehouseId))
                .orElseThrow(() -> {
                    log.warn("Warehouse not found with id: {}", warehouseId);
                    return new WarehouseDomainException("Warehouse not found.");
                });
    }

    private Product getProductById(UUID productId) {
        return productRepository.findById(new ProductId(productId))
                .orElseThrow(() -> new WarehouseDomainException("Product not found with id: " + productId));
    }

    private Optional<Stock> getStock(UUID productId, UUID warehouseId) {
        return stockRepository.findByWarehouseIdAndProductId(new WarehouseId(warehouseId), new ProductId(productId));
    }

    private void saveStock(Stock stock) {
        Stock savedStock = stockRepository.save(stock);
        if (savedStock == null) {
            log.error("Could not save stock with product id: {} in warehouse id: {}", stock.getProductId().getValue(), stock.getWarehouseId().getValue());
            throw new WarehouseDomainException("Failed to save stock.");
        }
        log.info("Stock is saved with id: {}", savedStock.getId().getValue());
    }


//    private void saveStockStockTransfer(StockTransfer stockTransfer) {
//        StockTransfer savedStockTransfer = stockRepository.saveStockTransfer(stockTransfer);
//        if (savedStockTransfer == null) {
//            log.error("Could not save stock transfer with product id: {} from warehouse id: {} to warehouse id: {}", stockTransfer.getProductId().getValue(), stockTransfer.getFromWarehouseId().getValue(), stockTransfer.getToWarehouseId().getValue());
//            throw new WarehouseDomainException("Failed to save stock transfer.");
//        }
//        log.info("Stock transfer is saved");
//    }
}
