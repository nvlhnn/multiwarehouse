package com.nvlhnn.order.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;
import com.nvlhnn.order.service.domain.event.OrderPaymentEvent;

public interface OrderPaymentUpdateMessagePublisher extends DomainEventPublisher<OrderPaymentEvent> {

}
