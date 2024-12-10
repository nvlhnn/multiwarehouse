package com.nvlhnn.order.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.user.kafka.avro.model.UserResponseAvroModel;
import com.nvlhnn.order.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.order.service.domain.ports.input.message.listener.user.UserServiceMessageListener;
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
public class UserRegisteredKafkaListener
        implements KafkaConsumer<UserResponseAvroModel>
{

    private final UserServiceMessageListener userServiceMessageListener;
    private final OrderMessagingDataMapper orderMessagingDataMapper;

    public UserRegisteredKafkaListener(
            UserServiceMessageListener userServiceMessageListener,
                                      OrderMessagingDataMapper orderMessagingDataMapper
    ) {
        this.userServiceMessageListener = userServiceMessageListener;
        this.orderMessagingDataMapper = orderMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.order-service-user-saved-consumer-group-id}",
            topics = "${order-service.user-registered-topic-name}")
    public void receive(@Payload List<UserResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of user responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(userResponseAvroModel -> {
            log.info("Processing user response with email: {}", userResponseAvroModel.getEmail());
            log.info("token : {}", userResponseAvroModel.getToken());
            UserResponseMessage userResponseMessage = orderMessagingDataMapper
                    .userAvroModelToUserResponseMessage(userResponseAvroModel);

            log.info("userResponseMessage token : {}", userResponseMessage.getToken());
            userServiceMessageListener.onUserRegistered(userResponseMessage);
        });

    }
}
