package com.nvlhnn.warehouse.service.messaging.publisher.kafka;

import com.nvlhnn.kafka.producer.KafkaMessageHelper;
import com.nvlhnn.kafka.producer.service.KafkaProducer;
import com.nvlhnn.warehouse.kafka.avro.model.StockCreatedAvroModel;
import com.nvlhnn.warehouse.kafka.avro.model.WarehouseCreatedAvroModel;
import com.nvlhnn.warehouse.service.domain.config.WarehouseServiceConfigData;
import com.nvlhnn.warehouse.service.domain.event.StockCreatedEvent;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockCreatedEventPublisher;
import com.nvlhnn.warehouse.service.messaging.mapper.WarehouseMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockCreatedKafkaMessagePublisher implements StockCreatedEventPublisher {

    private final WarehouseMessagingDataMapper warehouseMessagingDataMapper;
    private final WarehouseServiceConfigData warehouseServiceConfigData;
    private final KafkaProducer<String, StockCreatedAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public StockCreatedKafkaMessagePublisher(WarehouseMessagingDataMapper warehouseMessagingDataMapper,
                                                 WarehouseServiceConfigData warehouseServiceConfigData,
                                                 KafkaProducer<String, StockCreatedAvroModel> kafkaProducer,
                                                 KafkaMessageHelper kafkaMessageHelper) {
        this.warehouseMessagingDataMapper = warehouseMessagingDataMapper;
        this.warehouseServiceConfigData = warehouseServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(StockCreatedEvent domainEvent) {

        String stockId = domainEvent.getStock().getId().getValue().toString();
        log.info("Received StockCreatedEvent for stock id: {}", stockId);

        try {
            StockCreatedAvroModel stockCreatedAvroModel = warehouseMessagingDataMapper
                    .stockCreatedEventToStockCreatedAvroModel(domainEvent);

            kafkaProducer.send(warehouseServiceConfigData.getStockCreatedTopicName(),
                    stockId,
                    stockCreatedAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            warehouseServiceConfigData.getStockCreatedTopicName(),
                            stockCreatedAvroModel,
                            stockId,
                            "StockCreatedAvroModel"
                    )
            );

            log.info("StockCreatedAvroModel sent to Kafka for stock id: {}", stockId);
        } catch (Exception e) {
            log.error("Error while sending StockCreatedAvroModel message to Kafka with stock id: {}, error: {}", stockId, e.getMessage());
        }


    }
}
