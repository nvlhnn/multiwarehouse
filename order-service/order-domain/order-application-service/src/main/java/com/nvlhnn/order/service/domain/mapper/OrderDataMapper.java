package com.nvlhnn.order.service.domain.mapper;

import com.nvlhnn.domain.valueobject.*;
import com.nvlhnn.order.service.domain.dto.create.CreateOrderCommand;
import com.nvlhnn.order.service.domain.dto.create.CreateOrderResponse;
import com.nvlhnn.order.service.domain.dto.create.OrderAddress;
import com.nvlhnn.order.service.domain.dto.message.WarehouseResponse;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderResponse;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.entity.OrderItem;
import com.nvlhnn.order.service.domain.entity.Product;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.valueobject.*;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {


    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand) {
        return Order.builder()
                .customerId(new CustomerId(createOrderCommand.getCustomerId()))
                .warehouseId(new WarehouseId(createOrderCommand.getWarehouseId()))
                .deliveryAddress(orderAddressToStreetAddress(createOrderCommand.getAddress()))
                .price(new Money(createOrderCommand.getPrice()))
                .items(orderItemsToOrderItemEntities(createOrderCommand.getItems()))
                .build();
    }


    public CreateOrderResponse orderToCreateOrderResponse(Order order, String message) {
        return CreateOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .orderId(order.getId().getValue())
                .message(message)
                .build();
    }


    public TrackOrderResponse orderToTrackOrderResponse(Order order) {
        return TrackOrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderStatus(order.getOrderStatus())
                .failureMessage(order.getFailureMessages())
                .build();
    }

    private List<OrderItem> orderItemsToOrderItemEntities(
            @NotNull List<com.nvlhnn.order.service.domain.dto.create.OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem ->
                        OrderItem.builder()
                                .product(new Product(new ProductId(orderItem.getProductId())))
                                .price(new Money(orderItem.getPrice()))
                                .quantity(orderItem.getQuantity())
                                .subTotal(new Money(orderItem.getSubTotal()))
                                .build()).collect(Collectors.toList());
    }

    private StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity()
        );
    }

    public Warehouse warehouseResponseToWarehouse(WarehouseResponse warehouseResponse) {
        return Warehouse.builder().
            warehouseId(new WarehouseId(UUID.fromString(warehouseResponse.getWarehoudId()))).
            name(warehouseResponse.getName()).
            active(warehouseResponse.getIsActive()).
            build();
    }
}
