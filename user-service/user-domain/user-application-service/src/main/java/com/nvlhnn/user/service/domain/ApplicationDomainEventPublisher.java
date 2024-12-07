package com.nvlhnn.user.service.domain;

import com.nvlhnn.domain.event.publisher.DomainEventPublisher;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ApplicationDomainEventPublisher implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * Publish a UserCreatedEvent
     *
     * @param domainEvent the event to publish
     */
    public void publish(UserCreatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("UserCreatedEvent published for user id: {}", domainEvent.getUser().getId());
    }

}
