package com.nvlhnn.order.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.Product;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;


@Slf4j
public class OrderDomainServiceImpl implements OrderDomainService {


    private static final String UTC = "UTC";

    @Override
    public OrderCreatedEvent validateAndInitiateOrder(Order order, Warehouse warehouse,
                                                      DomainEventPublisher<OrderCreatedEvent>
                                                              orderCreatedEventDomainEventPublisher) {
        validateSeller(warehouse);
//        setOrderProductInformation(order, warehouse);
        order.validateOrder();
        order.initializeOrder();
        log.info("Order with id: {} is initiated", order.getId().getValue());
        return new OrderCreatedEvent(order, ZonedDateTime.now(ZoneId.of(UTC)), orderCreatedEventDomainEventPublisher);
    }

    @Override
    public void validateInitialWarehouse(Warehouse warehouse) {
        warehouse.validateInitializeWarehouse();
        log.info("Warehouse with id: {} is initialized", warehouse.getId().getValue());
    }


    private void validateSeller(Warehouse warehuse) {
        if (!warehuse.isActive()) {
            throw new OrderDomainException("Seller with id " + warehuse.getId().getValue() +
                    " is currently not active!");
        }
    }


}
