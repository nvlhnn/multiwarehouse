package com.nvlhnn.product.service.domain;

import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;
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
     * Publish a ProductreatedEvent
     *
     * @param domainEvent the event to publish
     */
    public void publish(ProductCreatedEvent domainEvent) {
        applicationEventPublisher.publishEvent(domainEvent);
        log.info("ProductCreatedEvent published for product id: {}", domainEvent.getProduct().getId());
    }

}
