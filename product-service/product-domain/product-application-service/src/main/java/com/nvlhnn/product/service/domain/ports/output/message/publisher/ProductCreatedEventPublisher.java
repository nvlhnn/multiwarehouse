
package com.nvlhnn.product.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;

public interface ProductCreatedEventPublisher extends DomainEventPublisher<ProductCreatedEvent> {
}
