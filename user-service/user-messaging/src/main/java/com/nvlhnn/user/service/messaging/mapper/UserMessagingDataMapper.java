package com.nvlhnn.user.service.messaging.mapper;

import com.nvlhnn.user.kafka.avro.model.UserResponseAvroModel;
import com.nvlhnn.user.kafka.avro.model.UserRole;
import com.nvlhnn.user.service.domain.entity.User;
import com.nvlhnn.user.service.domain.event.UserCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class UserMessagingDataMapper {

//        public UserResponseMessage userResponseAvroModelToUserResponse(UserResponseAvroModel avroModel) {
//            return UserResponseMessage.builder()
//                    .email(avroModel.getEmail())
//                    .name(avroModel.getName())
//                    .role(avroModel.getRole())
//                    .token(avroModel.getToken())
//                    .isActive(avroModel.getIsActive())
//                    .eventTimestamp(avroModel.getEventTimestamp())
//                    .build();
//        }

    public UserResponseAvroModel userCreatedEventToUserResponseAvroModel(UserCreatedEvent userCreatedEvent) {
        User user = userCreatedEvent.getUser();

        return UserResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setEmail(user.getEmail())
                .setUserId(user.getId().getValue())
                .setName(user.getName())
                .setSagaId(UUID.randomUUID())
                .setRole(mapUserRole(user.getRole()))
                .setToken(user.getToken() != null ? user.getToken() : "")
                .setIsActive(user.isActive())
                .setEventTimestamp(userCreatedEvent.getCreatedAt().toInstant())
                .build();
    }


    private UserRole mapUserRole(com.nvlhnn.domain.valueobject.UserRole userRole) {
        return switch (userRole) {
            case CUSTOMER -> UserRole.CUSTOMER;
            case WAREHOUSE_ADMIN -> UserRole.WAREHOUSE_ADMIN;
            case SUPER_ADMIN -> UserRole.SUPER_ADMIN;
        };
    }
}
