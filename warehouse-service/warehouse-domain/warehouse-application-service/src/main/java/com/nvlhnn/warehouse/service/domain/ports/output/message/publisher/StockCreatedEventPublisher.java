package com.nvlhnn.warehouse.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;

public interface StockCreatedEventPublisher extends DomainEventPublisher<StockCreatedEvent> {
}
