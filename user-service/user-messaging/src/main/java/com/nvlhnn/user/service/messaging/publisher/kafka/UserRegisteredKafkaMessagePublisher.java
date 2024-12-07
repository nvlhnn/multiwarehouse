package com.nvlhnn.user.service.messaging.publisher.kafka;

import com.nvlhnn.kafka.producer.service.KafkaProducer;
import com.nvlhnn.user.kafka.avro.model.UserResponseAvroModel;
import com.nvlhnn.user.service.domain.config.UserServiceConfigData;
import com.nvlhnn.kafka.producer.KafkaMessageHelper;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;
import com.nvlhnn.user.service.domain.ports.output.message.publisher.UserCreatedEventPublisher;
import com.nvlhnn.user.service.messaging.mapper.UserMessagingDataMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UserRegisteredKafkaMessagePublisher implements UserCreatedEventPublisher {

    private final UserMessagingDataMapper userMessagingDataMapper;
    private final UserServiceConfigData userServiceConfigData;
    private final KafkaProducer<String, UserResponseAvroModel> kafkaProducer;
    private final KafkaMessageHelper kafkaMessageHelper;

    public UserRegisteredKafkaMessagePublisher(UserMessagingDataMapper userMessagingDataMapper,
                                               UserServiceConfigData userServiceConfigData,
                                               KafkaProducer<String, UserResponseAvroModel> kafkaProducer,
                                               KafkaMessageHelper kafkaMessageHelper) {
        this.userMessagingDataMapper = userMessagingDataMapper;
        this.userServiceConfigData = userServiceConfigData;
        this.kafkaProducer = kafkaProducer;
        this.kafkaMessageHelper = kafkaMessageHelper;
    }

    @Override
    public void publish(UserCreatedEvent domainEvent) {
        String userId = domainEvent.getUser().getId().getValue().toString();
        log.info("Received UserRegisteredEvent for user id: {}", userId);

        try {
            UserResponseAvroModel userResponseAvroModel = userMessagingDataMapper
                    .userCreatedEventToUserResponseAvroModel(domainEvent);

            kafkaProducer.send(userServiceConfigData.getUserRegisteredTopicName(),
                    userId,
                    userResponseAvroModel,
                    kafkaMessageHelper.getKafkaCallback(
                            userServiceConfigData.getUserRegisteredTopicName(),
                            userResponseAvroModel,
                            userId,
                            "UserResponseAvroModel"
                    )
            );

            log.info("UserResponseAvroModel sent to Kafka for user id: {}", userId);
        } catch (Exception e) {
            log.error("Error while sending UserResponseAvroModel message to Kafka with user id: {}, error: {}", userId, e.getMessage());
        }
    }
}
