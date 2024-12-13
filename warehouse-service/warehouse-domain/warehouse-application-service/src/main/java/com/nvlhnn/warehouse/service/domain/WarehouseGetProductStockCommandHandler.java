package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.response.WarehouseProductStockResponse;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class WarehouseGetProductStockCommandHandler {

    private final StockRepository stockRepository;
    private final WarehouseDataMapper warehouseDataMapper;

    public WarehouseGetProductStockCommandHandler(StockRepository stockRepository, WarehouseDataMapper warehouseDataMapper) {
        this.stockRepository = stockRepository;
        this.warehouseDataMapper = warehouseDataMapper;
    }

    public WarehouseProductStockResponse getProductStockByWarehouse(UUID warehouseId, UUID productId) {
        log.info("Getting stock for product id: {} in warehouse id: {}", productId, warehouseId);

        Optional<Stock> stock = stockRepository.findByWarehouseIdAndProductId(new WarehouseId(warehouseId), new ProductId(productId));
        if (stock.isEmpty()) {
            return WarehouseProductStockResponse.builder()
                    .warehouseId(warehouseId)
                    .productId(productId)
                    .quantity(0)
                    .build();
        }else {
            return WarehouseProductStockResponse.builder()
                    .warehouseId(stock.get().getWarehouseId().getValue())
                    .productId(stock.get().getProductId().getValue())
                    .quantity(stock.get().getQuantity())
                    .build();
        }

    }
}
