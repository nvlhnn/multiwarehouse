package com.nvlhnn.order.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.product.kafka.avro.model.ProductResponseAvroModel;
import com.nvlhnn.order.service.domain.dto.message.ProductResponseMessage;
import com.nvlhnn.order.service.domain.ports.input.message.listener.product.ProductServiceMessageListener;
import com.nvlhnn.order.service.messaging.mapper.OrderMessagingDataMapper;
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
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public ProductCreatedKafkaListener(
            ProductServiceMessageListener productServiceMessageListener,
                                      OrderMessagingDataMapper orderMessagingDataMapper
    ) {
        this.productServiceMessageListener = productServiceMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.order-service-product-saved-consumer-group-id}",
            topics = "${order-service.product-created-topic-name}")
    public void receive(@Payload List<ProductResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of product responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(productResponseAvroModel -> {
            log.info("Processing product response with id: {}", productResponseAvroModel.getProductId());
            ProductResponseMessage productResponseMessage = orderMessagingDataMapper
                    .productResponseMessageToProduct(productResponseAvroModel);
            productServiceMessageListener.onProductCreated(productResponseMessage);
        });

    }
}
