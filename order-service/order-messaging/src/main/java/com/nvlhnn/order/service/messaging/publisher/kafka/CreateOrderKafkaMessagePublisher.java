package com.nvlhnn.order.service.messaging.publisher.kafka;

import com.nvlhnn.kafka.producer.KafkaMessageHelper;
import com.nvlhnn.kafka.producer.service.KafkaProducer;
import com.nvlhnn.order.service.domain.config.OrderServiceConfigData;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;
import com.nvlhnn.order.service.domain.ports.output.message.publisher.OrderCreatedPaymentRequestMessagePublisher;
import com.nvlhnn.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderResponseAvroModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreateOrderKafkaMessagePublisher implements OrderCreatedPaymentRequestMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, OrderResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper orderKafkaMessageHelper;

    public CreateOrderKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                            OrderServiceConfigData orderServiceConfigData,
                                            KafkaProducer<String, OrderResponseAvroModel> kafkaProducer,
                                            KafkaMessageHelper kafkaMessageHelper) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderCreatedEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("Received OrderCreatedEvent for order id: {}", orderId);

        try {
            OrderResponseAvroModel orderAvromodel = orderMessagingDataMapper
                    .orderCreatedEventToOrderResponseAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getOrderResponseTopicName(),
                    orderId,
                    orderAvromodel,
                    orderKafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getOrderResponseTopicName(),
                                    orderAvromodel,
                                    orderId,
                                    "OrderResponseAvroModel"));

            log.info("PaymentRequestAvroModel sent to Kafka for warehouse id: {}", orderAvromodel.getWarehouseId());
        } catch (Exception e) {
            log.error("Error while sending OrderResponseAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
