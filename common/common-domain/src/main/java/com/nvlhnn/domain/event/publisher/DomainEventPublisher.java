package com.nvlhnn.domain.event.publisher;

import com.nvlhnn.domain.event.DomainEvent;

public interface DomainEventPublisher <T extends DomainEvent> {

    void publish(T domainEvent);
}
