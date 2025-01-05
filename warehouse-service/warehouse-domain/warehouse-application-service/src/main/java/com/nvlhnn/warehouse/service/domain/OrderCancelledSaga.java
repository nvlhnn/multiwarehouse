package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.domain.valueobject.ProductId;
import com.nvlhnn.warehouse.service.domain.dto.message.PaymentResponse;
import com.nvlhnn.warehouse.service.domain.entity.OrderStockMutation;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.OrderStockMutationRepository;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.StockRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderCancelledSaga {

    private final WarehouseDomainService warehouseDomainService;
    private final OrderStockMutationRepository orderStockMutationRepository;
    private final StockRepository stockRepository;
    private final WarehouseSagaHelper warehouseSagaHelper;
    private final StockUpdatedEventPublisher stockUpdatedEventPublisher;

    public OrderCancelledSaga(WarehouseDomainService warehouseDomainService, OrderStockMutationRepository orderStockMutationRepository, StockRepository stockRepository, WarehouseSagaHelper warehouseSagaHelper, StockUpdatedEventPublisher stockUpdatedEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.orderStockMutationRepository = orderStockMutationRepository;
        this.stockRepository = stockRepository;
        this.warehouseSagaHelper = warehouseSagaHelper;
        this.stockUpdatedEventPublisher = stockUpdatedEventPublisher;
    }

    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<List<OrderStockMutation>> orderStockMutations = orderStockMutationRepository.findByOrderId(new OrderId(UUID.fromString(paymentResponse.getOrderId())));
        if (orderStockMutations.isPresent()) {
            orderStockMutations.get().forEach(orderStockMutation -> {
                warehouseDomainService.stockMutationCancelled(orderStockMutation);
                orderStockMutationRepository.save(orderStockMutation);

                // update stock back
                Optional<Stock> stock = warehouseSagaHelper.findStock(orderStockMutation.getWarehouseId().getValue().toString(), orderStockMutation.getProductId().getValue().toString());
                if (stock.isPresent()) {
                    Stock foundStock = stock.get();

                    StockUpdatedEvent stockUpdatedEvent = warehouseDomainService.validateAndPatchStock(foundStock, orderStockMutation.getQuantity(), stockUpdatedEventPublisher);
                    warehouseSagaHelper.saveStock(foundStock);

                    Integer productTotalQuantity = warehouseSagaHelper.getProductTotalQuantity(foundStock.getProductId());
                    productTotalQuantity = productTotalQuantity != null ? productTotalQuantity : 0;
                    stockUpdatedEvent.setProductTotalQuantity(productTotalQuantity);


                    stockUpdatedEventPublisher.publish(stockUpdatedEvent);
                    log.info("Stock for product id: {} has been updated with quantity: {} after order cancelled", foundStock.getProductId().getValue().toString(), foundStock.getQuantity());
                }

            });
        }

    }
}
