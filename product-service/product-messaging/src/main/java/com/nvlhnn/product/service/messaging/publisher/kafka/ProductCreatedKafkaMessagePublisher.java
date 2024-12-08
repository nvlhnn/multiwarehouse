package com.nvlhnn.product.service.messaging.publisher.kafka;

import com.nvlhnn.kafka.producer.service.KafkaProducer;
import com.nvlhnn.product.kafka.avro.model.ProductResponseAvroModel;
import com.nvlhnn.product.service.domain.config.ProductServiceConfigData;
import com.nvlhnn.product.service.domain.event.ProductCreatedEvent;
import com.nvlhnn.product.service.domain.ports.output.message.publisher.ProductCreatedEventPublisher;
import com.nvlhnn.kafka.producer.KafkaMessageHelper;
import com.nvlhnn.product.service.messaging.mapper.ProductMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductCreatedKafkaMessagePublisher implements ProductCreatedEventPublisher {

    private final ProductMessagingDataMapper productMessagingDataMapper;
    private final ProductServiceConfigData productServiceConfigData;
    private final KafkaProducer<String, ProductResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public ProductCreatedKafkaMessagePublisher(
            ProductMessagingDataMapper productMessagingDataMapper,
                                               ProductServiceConfigData productServiceConfigData,
                                               KafkaProducer<String, ProductResponseAvroModel> kafkaProducer,
                                               KafkaMessageHelper kafkaMessageHelper
    ) {
        this.productMessagingDataMapper = productMessagingDataMapper;
        this.productServiceConfigData = productServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(ProductCreatedEvent domainEvent) {
        String productId = domainEvent.getProduct().getId().getValue().toString();
        log.info("Received ProductCreatedEvent for product id: {}", productId);

        try {
            ProductResponseAvroModel productCreatedAvroModel = productMessagingDataMapper
                    .productCreatedEventToProductResponseAvroModel(domainEvent);

            kafkaProducer.send(productServiceConfigData.getProductCreatedTopicName(),
                    productId,
                    productCreatedAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            productServiceConfigData.getProductCreatedTopicName(),
                            productCreatedAvroModel,
                            productId,
                            "ProductResponseAvroModel"
                    )
            );

            log.info("ProductResponseAvroModel sent to Kafka for product id: {}", productId);
        } catch (Exception e) {
            log.error("Error while sending ProductResponseAvroModel message to Kafka with product id: {}, error: {}", productId, e.getMessage());
        }
    }
}
