package com.nvlhnn.warehouse.service.messaging.publisher.kafka;

import com.nvlhnn.kafka.producer.service.KafkaProducer;
import com.nvlhnn.warehouse.kafka.avro.model.WarehouseCreatedAvroModel;
import com.nvlhnn.warehouse.service.domain.config.WarehouseServiceConfigData;
import com.nvlhnn.warehouse.service.domain.event.WarehouseCreatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.WarehouseCreatedEventPublisher;
import com.nvlhnn.kafka.producer.KafkaMessageHelper;
import com.nvlhnn.warehouse.service.messaging.mapper.WarehouseMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarehouseCreatedKafkaMessagePublisher implements WarehouseCreatedEventPublisher {

    private final WarehouseMessagingDataMapper warehouseMessagingDataMapper;
    private final WarehouseServiceConfigData warehouseServiceConfigData;
    private final KafkaProducer<String, WarehouseCreatedAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public WarehouseCreatedKafkaMessagePublisher(WarehouseMessagingDataMapper warehouseMessagingDataMapper,
                                                 WarehouseServiceConfigData warehouseServiceConfigData,
                                                 KafkaProducer<String, WarehouseCreatedAvroModel> kafkaProducer,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.warehouseMessagingDataMapper = warehouseMessagingDataMapper;
        this.warehouseServiceConfigData = warehouseServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(WarehouseCreatedEvent domainEvent) {
        String warehouseId = domainEvent.getWarehouse().getId().getValue().toString();
        log.info("Received WarehouseCreatedEvent for warehouse id: {}", warehouseId);

        try {
            WarehouseCreatedAvroModel warehouseCreatedAvroModel = warehouseMessagingDataMapper
                    .warehouseCreatedEventToAvroModel(domainEvent);

            kafkaProducer.send(warehouseServiceConfigData.getWarehouseCreatedTopicName(),
                    warehouseId,
                    warehouseCreatedAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            warehouseServiceConfigData.getWarehouseCreatedTopicName(),
                            warehouseCreatedAvroModel,
                            warehouseId,
                            "WarehouseCreatedAvroModel"
                    )
            );

            log.info("WarehouseCreatedAvroModel sent to Kafka for warehouse id: {}", warehouseId);
        } catch (Exception e) {
            log.error("Error while sending WarehouseCreatedAvroModel message to Kafka with warehouse id: {}, error: {}", warehouseId, e.getMessage());
        }
    }
}
