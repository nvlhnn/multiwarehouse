package com.nvlhnn.order.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.Stock;
import com.nvlhnn.order.service.domain.entity.User;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;

import java.util.List;
import java.util.Optional;

public interface OrderDomainService {
    OrderCreatedEvent validateAndInitiateOrder(Order order, List<Stock> stocks,
                                               Warehouse nearestWarehouse,
                                               DomainEventPublisher<OrderCreatedEvent> orderCreatedEventDomainEventPublisher);

    void validateInitialWarehouse(Warehouse warehouse);

    void createUser(User user);

    void payOrder(Order order);

}
