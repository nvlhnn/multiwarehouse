package com.nvlhnn.warehouse.service.messaging.mapper;

import com.nvlhnn.user.kafka.avro.model.UserResponseAvroModel;
import com.nvlhnn.warehouse.kafka.avro.model.WarehouseCreatedAvroModel;
import com.nvlhnn.warehouse.kafka.order.avro.model.OrderResponseAvroModel;
import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.WarehouseCreatedEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class WarehouseMessagingDataMapper {

    public OrderResponse orderResponseAvroModelToOrderResponse(OrderResponseAvroModel orderResponseAvroModel) {
        return OrderResponse.builder()
                .id(orderResponseAvroModel.getId().toString())
                .sagaId(orderResponseAvroModel.getSagaId().toString())
                .orderId(orderResponseAvroModel.getOrderId().toString())
                .warehoudId(orderResponseAvroModel.getWarehouseId().toString())
                .createdAt(orderResponseAvroModel.getCreatedAt())
                .products(orderResponseAvroModel.getProducts().stream()
                        .map(product -> new OrderResponse.ProductQuantity(
                                product.getProductId().toString(),
                                product.getStockId().toString(),
                                product.getQuantity()))
                        .collect(Collectors.toList()))
                .build();
    }

    public WarehouseCreatedAvroModel warehouseCreatedEventToAvroModel(WarehouseCreatedEvent warehouseCreatedEvent) {
        Warehouse warehouse = warehouseCreatedEvent.getWarehouse();

        return WarehouseCreatedAvroModel.newBuilder()
                .setId(UUID.randomUUID())
                .setWarehouseId(warehouse.getId().getValue())
                .setName(warehouse.getName())
                .setIsActive(warehouse.isActive())
                .setSagaId(UUID.randomUUID())
                .setCreatedAt(warehouseCreatedEvent.getCreatedAt().toInstant())
                .setAddress(com.nvlhnn.warehouse.kafka.avro.model.WarehouseAddress.newBuilder()
                        .setStreet(warehouse.getStreetAddress().getStreet())
                        .setPostalCode(warehouse.getStreetAddress().getPostalCode())
                        .setCity(warehouse.getStreetAddress().getCity())
                        .setLatitude(warehouse.getStreetAddress().getLatitude())
                        .setLongitude(warehouse.getStreetAddress().getLongitude())
                        .build())
                .build();
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