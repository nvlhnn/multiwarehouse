package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

        Warehouse warehouse = sagaHelper.findWarehouse(orderResponse.getWarehoudId());
        for (OrderResponse.ProductQuantity product : orderResponse.getProducts()) {
            Stock stock = sagaHelper.findStock(orderResponse.getWarehoudId(), product.getProductId())
                    .orElseThrow(() -> new WarehouseDomainException("Stock not found for product " + product.getProductId()));

            warehouseDomainService.updateStock(stock, -(product.getQuantity()), stockUpdatedEventPublisher);
            sagaHelper.saveStock(stock);

            log.info("Updated stock for product id: {} in warehouse id: {} with new quantity: {}",
                    product.getProductId(), warehouse.getId().getValue(), stock.getQuantity());
        }
    }
}
