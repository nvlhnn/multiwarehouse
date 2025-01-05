package com.nvlhnn.order.service.messaging.publisher.kafka;

import com.nvlhnn.kafka.producer.KafkaMessageHelper;
import com.nvlhnn.kafka.producer.service.KafkaProducer;
import com.nvlhnn.order.service.domain.config.OrderServiceConfigData;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;
import com.nvlhnn.order.service.domain.event.OrderPaymentEvent;
import com.nvlhnn.order.service.domain.ports.output.message.publisher.OrderCreatedPaymentRequestMessagePublisher;
import com.nvlhnn.order.service.domain.ports.output.message.publisher.OrderPaymentUpdateMessagePublisher;
import com.nvlhnn.order.service.messaging.mapper.OrderMessagingDataMapper;
import com.nvlhnn.payment.kafka.avro.model.PaymentResponseAvroModel;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderResponseAvroModel;
import com.nvlhnn.websocket.publisher.WebSocketPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderPaymentKafkaMessagePublisher implements OrderPaymentUpdateMessagePublisher {

    private final OrderMessagingDataMapper orderMessagingDataMapper;
    private final OrderServiceConfigData orderServiceConfigData;
    private final KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper orderKafkaMessageHelper;

    public OrderPaymentKafkaMessagePublisher(OrderMessagingDataMapper orderMessagingDataMapper,
                                             OrderServiceConfigData orderServiceConfigData,
                                             KafkaProducer<String, PaymentResponseAvroModel> kafkaProducer,
                                             KafkaMessageHelper kafkaMessageHelper
                                            ) {
        this.orderMessagingDataMapper = orderMessagingDataMapper;
        this.orderServiceConfigData = orderServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.orderKafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(OrderPaymentEvent domainEvent) {
        String orderId = domainEvent.getOrder().getId().getValue().toString();
        log.info("publishing OrderPaymentEvent for order id: {}", orderId);

        try {
            PaymentResponseAvroModel paymentResponseAvroModel = orderMessagingDataMapper
                    .orderToPaymentResponseAvroModel(domainEvent);

            kafkaProducer.send(orderServiceConfigData.getPaymentResponseTopicName(),
                    orderId,
                    paymentResponseAvroModel,
                    orderKafkaMessageHelper
                            .getKafkaCallback(orderServiceConfigData.getPaymentResponseTopicName(),
                                    paymentResponseAvroModel,
                                    orderId,
                                    "paymentResponseAvroModel"));

            log.info("PaymentRequestAvroModel sent to Kafka for order id: {}", paymentResponseAvroModel.getOrderId());
        } catch (Exception e) {
            log.error("Error while sending OrderResponseAvroModel message" +
                    " to kafka with order id: {}, error: {}", orderId, e.getMessage());
        }
    }
}
