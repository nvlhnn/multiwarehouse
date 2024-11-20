package com.nvlhnn.order.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;

public interface OrderCreatedPaymentRequestMessagePublisher extends DomainEventPublisher<OrderCreatedEvent> {

}
