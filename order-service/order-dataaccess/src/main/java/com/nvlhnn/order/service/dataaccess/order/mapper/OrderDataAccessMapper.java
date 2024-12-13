package com.nvlhnn.order.service.dataaccess.order.mapper;

import com.nvlhnn.domain.valueobject.*;
import com.nvlhnn.order.service.dataaccess.order.entity.OrderAddressEntity;
import com.nvlhnn.order.service.dataaccess.order.entity.OrderEntity;
import com.nvlhnn.order.service.dataaccess.order.entity.OrderItemEntity;
import com.nvlhnn.order.service.dataaccess.product.entity.ProductEntity;
import com.nvlhnn.order.service.dataaccess.warehouse.entity.WarehouseEntity;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.OrderItem;
import com.nvlhnn.order.service.domain.entity.Product;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.valueobject.OrderItemId;
import com.nvlhnn.order.service.domain.valueobject.StreetAddress;
import com.nvlhnn.order.service.domain.valueobject.TrackingId;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.nvlhnn.order.service.domain.entity.Order.FAILURE_MESSAGE_DELIMITER;

@Component
public class OrderDataAccessMapper {

    public OrderEntity orderToOrderEntity(Order order) {
        OrderEntity orderEntity = OrderEntity.builder()
                .id(order.getId().getValue())
                .userId(order.getUserId().getValue())
                .warehouse(order.getWarehouse().getId() != null ? WarehouseEntity.builder()
                        .id(order.getWarehouse().getId().getValue())
                        .build() : null)
                .trackingId(order.getTrackingId().getValue())
                .address(deliveryAddressToAddressEntity(order.getDeliveryAddress()))
                .price(order.getPrice().getAmount())
                .items(orderItemsToOrderItemEntities(order.getItems()))
                .orderStatus(order.getOrderStatus())
                .failureMessages(order.getFailureMessages() != null ?
                        String.join(Order.FAILURE_MESSAGE_DELIMITER, order.getFailureMessages()) : "")
                .build();

        orderEntity.getAddress().setOrder(orderEntity);
        orderEntity.getItems().forEach(orderItemEntity -> orderItemEntity.setOrder(orderEntity));

        return orderEntity;
    }

    public Order orderEntityToOrder(OrderEntity orderEntity) {
        return Order.builder()
                .orderId(new OrderId(orderEntity.getId()))
                .userId(new UserId(orderEntity.getUserId()))
                .warehouse(orderEntity.getWarehouse() != null ? warehouseEntityToWarehouse(orderEntity.getWarehouse()) : null)
                .deliveryAddress(addressEntityToDeliveryAddress(orderEntity.getAddress()))
                .price(new Money(orderEntity.getPrice()))
                .items(orderItemEntitiesToOrderItems(orderEntity.getItems()))
                .trackingId(new TrackingId(orderEntity.getTrackingId()))
                .orderStatus(orderEntity.getOrderStatus())
                .failureMessages(orderEntity.getFailureMessages().isEmpty() ? new ArrayList<>() :
                        new ArrayList<>(Arrays.asList(orderEntity.getFailureMessages()
                                .split(Order.FAILURE_MESSAGE_DELIMITER))))
                .build();
    }

    private List<OrderItem> orderItemEntitiesToOrderItems(List<OrderItemEntity> items) {
        return items.stream()
                .map(orderItemEntity -> OrderItem.builder()
                        .orderItemId(new OrderItemId(orderItemEntity.getId()))
                        .product(new Product(
                                new ProductId(orderItemEntity.getProduct().getId()),
                                orderItemEntity.getProduct().getName(),
                                orderItemEntity.getProduct().getPrice()
                        ))
                        .price(new Money(orderItemEntity.getPrice()))
                        .quantity(orderItemEntity.getQuantity())
                        .subTotal(new Money(orderItemEntity.getSubTotal()))
                        .build())
                .collect(Collectors.toList());
    }

    private Warehouse warehouseEntityToWarehouse(WarehouseEntity warehouseEntity) {
        return Warehouse.builder()
                .warehouseId(new WarehouseId(warehouseEntity.getId()))
                .name(warehouseEntity.getName())
                .city(warehouseEntity.getCity())
                .latitude(warehouseEntity.getLatitude())
                .longitude(warehouseEntity.getLongitude())
                .build();
    }

    private StreetAddress addressEntityToDeliveryAddress(OrderAddressEntity address) {
        return new StreetAddress(address.getId(),
                address.getStreet(),
                address.getPostalCode(),
                address.getCity(),
                address.getLatitude(),
                address.getLongitude());
    }

    private List<OrderItemEntity> orderItemsToOrderItemEntities(List<OrderItem> items) {
        return items.stream()
                .map(orderItem -> OrderItemEntity.builder()
                        .id(orderItem.getId().getValue())
                        .product(ProductEntity.builder()
                                .id(orderItem.getProduct().getId().getValue())
                                .build())
                        .price(orderItem.getPrice().getAmount())
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .build())
                .collect(Collectors.toList());
    }

    private OrderAddressEntity deliveryAddressToAddressEntity(StreetAddress deliveryAddress) {
        return OrderAddressEntity.builder()
                .id(deliveryAddress.getId())
                .street(deliveryAddress.getStreet())
                .postalCode(deliveryAddress.getPostalCode())
                .city(deliveryAddress.getCity())
                .latitude(deliveryAddress.getLatitude())
                .longitude(deliveryAddress.getLongitude())
                .build();
    }
}

