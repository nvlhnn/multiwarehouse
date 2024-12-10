package com.nvlhnn.warehouse.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderResponseAvroModel;
import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.order.OrderResponseMessageListener;
import com.nvlhnn.warehouse.service.messaging.mapper.WarehouseMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class OrderResponseKafkaListener
        implements KafkaConsumer<OrderResponseAvroModel>
{

    private final OrderResponseMessageListener orderResponseMessageListener;
    private final WarehouseMessagingDataMapper warehouseMessagingDataMapper;

    public OrderResponseKafkaListener(
            OrderResponseMessageListener orderResponseMessageListener,
                                      WarehouseMessagingDataMapper warehouseMessagingDataMapper
    ) {
        this.orderResponseMessageListener = orderResponseMessageListener;
        this.warehouseMessagingDataMapper = warehouseMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.warehouse-service-order-response-group-id}",
            topics = "${warehouse-service.order-response-topic-name}")
    public void receive(@Payload List<OrderResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {
        log.info("{} number of order responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(orderResponseAvroModel -> {
            log.info("Processing order response with id: {}", orderResponseAvroModel.getId());
            OrderResponse orderResponseDto = warehouseMessagingDataMapper
                    .orderResponseAvroModelToOrderResponse(orderResponseAvroModel);
            orderResponseMessageListener.onOrderCreated(orderResponseDto);
        });
    }
}
