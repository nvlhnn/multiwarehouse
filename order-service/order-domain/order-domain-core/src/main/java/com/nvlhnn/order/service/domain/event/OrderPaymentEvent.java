package com.nvlhnn.order.service.domain.event;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.Warehouse;

import java.time.ZonedDateTime;

public class OrderPaymentEvent extends OrderEvent{

    private final DomainEventPublisher<OrderPaymentEvent> orderPaymentEventDomainEventPublisher;

    public OrderPaymentEvent(Order order,
                             ZonedDateTime createdAt,
                             DomainEventPublisher<OrderPaymentEvent> orderPaymentEventDomainEventPublisher) {
        super(order, createdAt);
        this.orderPaymentEventDomainEventPublisher = orderPaymentEventDomainEventPublisher;
    }

    @Override
    public void fire() {
        orderPaymentEventDomainEventPublisher.publish(this);
    }
}
