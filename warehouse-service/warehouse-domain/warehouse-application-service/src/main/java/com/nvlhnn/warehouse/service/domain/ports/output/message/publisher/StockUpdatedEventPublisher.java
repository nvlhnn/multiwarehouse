package com.nvlhnn.warehouse.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;

public interface StockUpdatedEventPublisher extends DomainEventPublisher<StockUpdatedEvent> {
}
