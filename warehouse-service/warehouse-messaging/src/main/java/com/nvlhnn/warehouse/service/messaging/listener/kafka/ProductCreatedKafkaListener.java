package com.nvlhnn.warehouse.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.product.kafka.avro.model.ProductResponseAvroModel;
import com.nvlhnn.product.kafka.avro.model.ProductResponseAvroModel;
import com.nvlhnn.warehouse.service.domain.dto.message.ProductResponseMessage;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.product.ProductServiceMessageListener;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.product.ProductServiceMessageListener;
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
public class ProductCreatedKafkaListener
        implements KafkaConsumer<ProductResponseAvroModel>
{

    private final ProductServiceMessageListener productServiceMessageListener;
    private final WarehouseMessagingDataMapper warehouseMessagingDataMapper;

    public ProductCreatedKafkaListener(
            ProductServiceMessageListener productServiceMessageListener,
                                      WarehouseMessagingDataMapper warehouseMessagingDataMapper
    ) {
        this.productServiceMessageListener = productServiceMessageListener;
        this.warehouseMessagingDataMapper = warehouseMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.warehouse-service-product-saved-consumer-group-id}",
            topics = "${warehouse-service.product-created-topic-name}")
    public void receive(@Payload List<ProductResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of product responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(productResponseAvroModel -> {
            log.info("Processing product response with id: {}", productResponseAvroModel.getProductId());
            ProductResponseMessage productResponseMessage = warehouseMessagingDataMapper
                    .productResponseMessageToProduct(productResponseAvroModel);
            productServiceMessageListener.onProductCreated(productResponseMessage);
        });

    }
}
