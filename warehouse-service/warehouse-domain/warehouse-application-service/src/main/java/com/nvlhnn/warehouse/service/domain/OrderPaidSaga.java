package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.warehouse.service.domain.dto.message.PaymentResponse;
import com.nvlhnn.warehouse.service.domain.entity.OrderStockMutation;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.OrderStockMutationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class OrderPaidSaga {

    private final WarehouseDomainService warehouseDomainService;
    private final OrderStockMutationRepository orderStockMutationRepository;

    public OrderPaidSaga(WarehouseDomainService warehouseDomainService, OrderStockMutationRepository orderStockMutationRepository) {
        this.warehouseDomainService = warehouseDomainService;
        this.orderStockMutationRepository = orderStockMutationRepository;
    }

    @Transactional
    public void process(PaymentResponse paymentResponse) {
        Optional<List<OrderStockMutation>> orderStockMutations = orderStockMutationRepository.findByOrderId(new OrderId(UUID.fromString(paymentResponse.getOrderId())));
        if (orderStockMutations.isPresent()) {
            orderStockMutations.get().forEach(orderStockMutation -> {
                warehouseDomainService.stockMutationPaid(orderStockMutation);
                orderStockMutationRepository.save(orderStockMutation);
            });
        }

        log.info("Received OrderPaid event for order id: {}", paymentResponse.getOrderId());
    }
}
