package com.nvlhnn.warehouse.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.payment.kafka.avro.model.OrderStatus;
import com.nvlhnn.payment.kafka.avro.model.PaymentResponseAvroModel;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderResponseAvroModel;
import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.dto.message.PaymentResponse;
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
public class PaymentResponseKafkaListener
        implements KafkaConsumer<PaymentResponseAvroModel>
{

    private final OrderResponseMessageListener orderResponseMessageListener;
    private final WarehouseMessagingDataMapper warehouseMessagingDataMapper;

    public PaymentResponseKafkaListener(
            OrderResponseMessageListener orderResponseMessageListener,
                                      WarehouseMessagingDataMapper warehouseMessagingDataMapper
    ) {
        this.orderResponseMessageListener = orderResponseMessageListener;
        this.warehouseMessagingDataMapper = warehouseMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.warehouse-service-payment-response-group-id}",
            topics = "${warehouse-service.payment-response-topic-name}")
    public void receive(@Payload List<PaymentResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of order responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(paymentResponseAvroModel -> {

            PaymentResponse paymentResponse = warehouseMessagingDataMapper.paymentResponseAvroModelToPaymentResponse(paymentResponseAvroModel);
            if (paymentResponseAvroModel.getOrderStatus() == OrderStatus.PAID) {
                log.info("Processing order paid with id: {}", paymentResponseAvroModel.getOrderId());
                orderResponseMessageListener.onOrderPaid(paymentResponse);
            }else if (paymentResponseAvroModel.getOrderStatus() == OrderStatus.CANCELLED) {
                log.info("Processing order cancelled with id: {}", paymentResponseAvroModel.getOrderId());
                orderResponseMessageListener.onOrderCancelled(paymentResponse);
            }
        });
    }
}
