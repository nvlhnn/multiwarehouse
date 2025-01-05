package com.nvlhnn.order.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.order.service.domain.dto.message.WarehouseResponseMessage;
import com.nvlhnn.order.service.domain.ports.input.message.listener.warehouse.WarehouseResponseMessageListener;
import com.nvlhnn.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.nvlhnn.warehouse.kafka.avro.model.WarehouseCreatedAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class WarehouseCreatedKafkaListener implements KafkaConsumer<WarehouseCreatedAvroModel>
{

    private final WarehouseResponseMessageListener warehouseResponseMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public WarehouseCreatedKafkaListener(
            WarehouseResponseMessageListener warehouseResponseMessageListener,
            OrderMessagingDataMapper orderMessagingDataMapper
    ) {
        this.warehouseResponseMessageListener = warehouseResponseMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.order-service-warehouse-saved-consumer-group-id}",
            topics = "${order-service.warehouse-created-topic-name}")
    public void receive(@Payload List<WarehouseCreatedAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of warehouse responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(warehouseResponseAvroModel -> {

            log.info("Processing warehouse response with id: {}", warehouseResponseAvroModel.getWarehouseId());
            WarehouseResponseMessage userResponseMessage = orderMessagingDataMapper
                    .warehouseCreatedAvroModelToWarehouseResponse(warehouseResponseAvroModel);

            warehouseResponseMessageListener.onWarehouseSave(userResponseMessage);

        });

    }
}