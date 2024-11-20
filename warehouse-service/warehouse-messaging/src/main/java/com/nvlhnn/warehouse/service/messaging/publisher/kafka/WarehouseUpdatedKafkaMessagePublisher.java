package com.nvlhnn.warehouse.service.messaging.publisher.kafka;

import com.nvlhnn.warehouse.service.domain.event.StockUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.event.WarehouseUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.WarehouseUpdatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarehouseUpdatedKafkaMessagePublisher implements WarehouseUpdatedEventPublisher {

    public WarehouseUpdatedKafkaMessagePublisher() {

    }

    @Override
    public void publish(WarehouseUpdatedEvent domainEvent) {

    }
}
