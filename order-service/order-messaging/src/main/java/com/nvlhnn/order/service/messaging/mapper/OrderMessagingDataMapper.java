package com.nvlhnn.order.service.messaging.mapper;

import com.nvlhnn.order.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.event.OrderCreatedEvent;
import com.nvlhnn.user.kafka.avro.model.UserResponseAvroModel;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderResponseAvroModel;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderStatus;
import com.nvlhnn.warehouse.kafka.order.avro.model.ProductQuantity;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderMessagingDataMapper {

    public OrderResponseAvroModel orderCreatedEventToOrderResponseAvroModel(OrderCreatedEvent orderCreatedEvent) {
        Order order = orderCreatedEvent.getOrder();

        return OrderResponseAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setSagaId(UUID.randomUUID())
                .setOrderId(order.getId().getValue())
                .setWarehouseId(order.getWarehouseId().getValue())
                .setCreatedAt(orderCreatedEvent.getCreatedAt().toInstant())
                .setOrderStatus(mapOrderStatus(order.getOrderStatus()))
                .setProducts(
                        order.getItems().stream()
                                .map(item -> ProductQuantity.newBuilder()
                                        .setProductId(item.getProduct().getId().getValue())
                                        .setStockId(item.getOrderId().getValue())
                                        .setQuantity(item.getQuantity())
                                        .build()
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    private OrderStatus mapOrderStatus(com.nvlhnn.domain.valueobject.OrderStatus orderStatus) {
        return switch (orderStatus) {
            case PENDING -> OrderStatus.PENDING;
            case PAID -> OrderStatus.PAID;
            case APPROVED -> OrderStatus.APPROVED;
            case CANCELLING -> OrderStatus.CANCELLING;
            case CANCELLED -> OrderStatus.CANCELLED;
        };
    }

    public UserResponseMessage userAvroModelToUserResponseMessage(UserResponseAvroModel userResponseMessage) {
        return UserResponseMessage.builder()
                .email(userResponseMessage.getEmail())
                .userId(userResponseMessage.getUserId().toString())
                .name(userResponseMessage.getName())
                .role(userResponseMessage.getRole().toString())
                .token(userResponseMessage.getToken())
                .isActive(userResponseMessage.getIsActive())
                .eventTimestamp(userResponseMessage.getEventTimestamp())
                .build();
    }

}