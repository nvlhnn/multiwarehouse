package com.nvlhnn.warehouse.service.messaging.listener.kafka;

import com.nvlhnn.kafka.consumer.KafkaConsumer;
import com.nvlhnn.user.kafka.avro.model.UserResponseAvroModel;
import com.nvlhnn.warehouse.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.user.UserServiceMessageListener;
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
public class UserRegisteredKafkaListener
        implements KafkaConsumer<UserResponseAvroModel>
{

    private final UserServiceMessageListener userServiceMessageListener;
    private final WarehouseMessagingDataMapper warehouseMessagingDataMapper;

    public UserRegisteredKafkaListener(
            UserServiceMessageListener userServiceMessageListener,
                                      WarehouseMessagingDataMapper warehouseMessagingDataMapper
    ) {
        this.userServiceMessageListener = userServiceMessageListener;
        this.warehouseMessagingDataMapper = warehouseMessagingDataMapper;
    }

    @Override
    @KafkaListener(id = "${kafka-consumer-config.user-service-consumer-group-id}",
            topics = "${warehouse-service.user-registered-topic-name}")
    public void receive(@Payload List<UserResponseAvroModel> messages,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) List<String> keys,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) List<Integer> partitions,
                        @Header(KafkaHeaders.OFFSET) List<Long> offsets) {

        log.info("{} number of user responses received with keys: {}, partitions: {}, and offsets: {}",
                messages.size(), keys.toString(), partitions.toString(), offsets.toString());

        messages.forEach(userResponseAvroModel -> {
            log.info("Processing user response with email: {}", userResponseAvroModel.getEmail());
            log.info("token : {}", userResponseAvroModel.getToken());
            UserResponseMessage userResponseMessage = warehouseMessagingDataMapper
                    .userAvroModelToUserResponseMessage(userResponseAvroModel);

            log.info("userResponseMessage token : {}", userResponseMessage.getToken());
            userServiceMessageListener.onUserRegistered(userResponseMessage);
        });

    }
}
