package com.nvlhnn.warehouse.service.messaging.publisher.kafka;

import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;
import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockCreatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockCreatedKafkaMessagePublisher implements StockCreatedEventPublisher {

    public StockCreatedKafkaMessagePublisher() {

    }

    @Override
    public void publish(StockCreatedEvent domainEvent) {

    }
}
