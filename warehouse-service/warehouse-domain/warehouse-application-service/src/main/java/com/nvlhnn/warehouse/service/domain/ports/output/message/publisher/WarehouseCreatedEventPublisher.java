package com.nvlhnn.warehouse.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.warehouse.service.domain.event.WarehouseCreatedEvent;

public interface WarehouseCreatedEventPublisher extends DomainEventPublisher<WarehouseCreatedEvent> {
}
