
package com.nvlhnn.user.service.domain.ports.output.message.publisher;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;

public interface UserCreatedEventPublisher extends DomainEventPublisher<UserCreatedEvent> {
}
