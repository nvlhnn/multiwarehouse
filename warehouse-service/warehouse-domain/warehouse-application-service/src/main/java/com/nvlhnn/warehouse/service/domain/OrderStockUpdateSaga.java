package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
public class OrderStockUpdateSaga {

    private final WarehouseDomainService warehouseDomainService;
    private final WarehouseSagaHelper sagaHelper;
    private final StockUpdatedEventPublisher stockUpdatedEventPublisher;

    public OrderStockUpdateSaga(WarehouseDomainService warehouseDomainService,
                                WarehouseSagaHelper sagaHelper,
                                StockUpdatedEventPublisher stockUpdatedEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.sagaHelper = sagaHelper;
        this.stockUpdatedEventPublisher = stockUpdatedEventPublisher;
    }

    @Transactional
    public void process(OrderResponse orderResponse) {
        log.info("Processing stock update for order id: {}", orderResponse.getOrderId());

        // Find the warehouse associated with the order
        Warehouse warehouse = findWarehouse(orderResponse);

        // Get the list of product IDs from the order response
        List<ProductId> productIds = getProductIdsFromOrderResponse(orderResponse);

        // Find all stocks related to the products in the order
        List<Stock> stocks = findStocksByProductIds(productIds);

        OrderId orderId = new OrderId(UUID.fromString(orderResponse.getOrderId()));

        // Process each product in the order and update stock
        for (OrderResponse.ProductQuantity product : orderResponse.getProducts()) {
            updateStockForProduct(warehouse, product, stocks, orderId);
        }
    }

    private Warehouse findWarehouse(OrderResponse orderResponse) {
        return sagaHelper.findWarehouse(orderResponse.getWarehoudId());
    }

    private List<ProductId> getProductIdsFromOrderResponse(OrderResponse orderResponse) {
        return orderResponse.getProducts().stream()
                .map(product -> new ProductId(UUID.fromString(product.getProductId())))
                .collect(Collectors.toList());
    }

    private List<Stock> findStocksByProductIds(List<ProductId> productIds) {
        Optional<List<Stock>> stocksOptional = sagaHelper.findByProductIdIn(productIds);
        if (stocksOptional.isEmpty()) {
            log.warn("Could not find stocks for products: {}", productIds);
            throw new WarehouseDomainException("Could not find stocks for products: " + productIds);
        }
        return stocksOptional.get();
    }

    private void updateStockForProduct(Warehouse warehouse, OrderResponse.ProductQuantity product, List<Stock> stocks, OrderId orderId) {
        ProductId productId = new ProductId(UUID.fromString(product.getProductId()));
        List<Stock> stocksToUpdate = findAndTransferStock(warehouse.getId(), stocks, productId, product.getQuantity());

        for (Stock stock : stocksToUpdate) {
            Stock currentStock = findStockForProduct(productId, stock);

            // Update stock and publish the event
            StockUpdatedEvent stockUpdatedEvent = warehouseDomainService.validateAndPatchStock(currentStock, stock.getQuantity(), stockUpdatedEventPublisher);
            sagaHelper.saveStock(currentStock);

            Integer productTotalQuantity = sagaHelper.getProductTotalQuantity(currentStock.getProductId());
            productTotalQuantity = productTotalQuantity != null ? productTotalQuantity : 0;
            stockUpdatedEvent.setProductTotalQuantity(productTotalQuantity);

            // save order stock mutation
            sagaHelper.saveOrderStockMutation(warehouseDomainService.validateAndInitiateOrderStockMutation(orderId, stock, -stock.getQuantity()));

            log.info("product total quantity is {}", productTotalQuantity);
            stockUpdatedEventPublisher.publish(stockUpdatedEvent);
            log.info("Updated stock for product id: {} in warehouse id: {} with new quantity: {}",
                    product.getProductId(), warehouse.getId().getValue(), stock.getQuantity());
        }
    }

    private Stock findStockForProduct(ProductId productId, Stock stock) {
        return sagaHelper.findStock(stock.getWarehouseId().getValue().toString(), productId.getValue().toString())
                .orElseThrow(() -> new WarehouseDomainException("Stock not found for product " + productId.getValue()));
    }

    private List<Stock> findAndTransferStock(WarehouseId warehouseId, List<Stock> stocks, ProductId productId, int requiredQuantity) {
        List<Stock> stockChanges = new java.util.ArrayList<>();
        Stock nearestWarehouseStock = findStockInWarehouse(warehouseId, productId, stocks);

        int stockInNearestWarehouse = (nearestWarehouseStock != null) ? nearestWarehouseStock.getQuantity() : 0;

        // Handle case where nearest warehouse has enough stock
        if (stockInNearestWarehouse >= requiredQuantity) {
            stockChanges.add(createStockChange(nearestWarehouseStock, -requiredQuantity));
            nearestWarehouseStock.updateQuantity(nearestWarehouseStock.getQuantity() - requiredQuantity);
            return stockChanges;
        }

        // Handle case where more stock is needed from other warehouses
        int remainingStockNeeded = requiredQuantity - stockInNearestWarehouse;
        if (stockInNearestWarehouse > 0) {
            stockChanges.add(createStockChange(nearestWarehouseStock, -stockInNearestWarehouse));
            nearestWarehouseStock.updateQuantity(0);
        }

        // Gather additional stock from other warehouses if needed
        List<Stock> stocksToReduce = gatherAdditionalStockFromOtherWarehouses(stocks, productId, warehouseId, remainingStockNeeded);

        // Apply the stock reductions from other warehouses
        for (Stock stock : stocksToReduce) {
            if (remainingStockNeeded <= 0) break;

            int stockToReduce = Math.min(stock.getQuantity(), remainingStockNeeded);
            stockChanges.add(createStockChange(stock, -stockToReduce));
            stock.updateQuantity(stock.getQuantity() - stockToReduce);
            remainingStockNeeded -= stockToReduce;
        }

        return stockChanges;
    }

    private Stock findStockInWarehouse(WarehouseId warehouseId, ProductId productId, List<Stock> stocks) {
        return stocks.stream()
                .filter(stock -> stock.getWarehouseId().equals(warehouseId) && stock.getProductId().equals(productId))
                .findFirst()
                .orElse(null);
    }

    private List<Stock> gatherAdditionalStockFromOtherWarehouses(List<Stock> stocks, ProductId productId, WarehouseId excludeWarehouseId, int remainingStockNeeded) {
        return stocks.stream()
                .filter(stock -> stock.getProductId().equals(productId))
                .filter(stock -> !stock.getWarehouseId().equals(excludeWarehouseId))
                .filter(stock -> stock.getQuantity() > 0)
                .sorted((stock1, stock2) -> Integer.compare(stock2.getQuantity(), stock1.getQuantity()))
                .collect(Collectors.toList());
    }

    private Stock createStockChange(Stock stock, int quantityChange) {
        return Stock.builder()
                .stockId(stock.getId())
                .warehouseId(stock.getWarehouseId())
                .productId(stock.getProductId())
                .quantity(quantityChange)
                .build();
    }
}
