package com.nvlhnn.order.service.domain.mapper;

import com.nvlhnn.domain.valueobject.*;
import com.nvlhnn.order.service.domain.dto.create.*;
import com.nvlhnn.order.service.domain.dto.message.*;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderResponse;
import com.nvlhnn.order.service.domain.entity.*;
import com.nvlhnn.order.service.domain.entity.OrderItem;
import com.nvlhnn.order.service.domain.valueobject.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderDataMapper {

    public Order createOrderCommandToOrder(CreateOrderCommand createOrderCommand, Warehouse nearestWarehouse, User user) {
        return Order.builder()
                .userId(user.getId())
                .warehouse(nearestWarehouse)
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

    public List<OrderItem> orderItemsToOrderItemEntities(
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

    public StreetAddress orderAddressToStreetAddress(OrderAddress orderAddress) {
        return new StreetAddress(
                UUID.randomUUID(),
                orderAddress.getStreet(),
                orderAddress.getPostalCode(),
                orderAddress.getCity(),
                orderAddress.getLatitude(),
                orderAddress.getLongitude()
        );
    }

    public Warehouse warehouseResponseToWarehouse(WarehouseResponseMessage warehouseResponseMessage) {
        return Warehouse.builder()
                .warehouseId(new WarehouseId(UUID.fromString(warehouseResponseMessage.getWarehoudId())))
                .name(warehouseResponseMessage.getName())
                .active(warehouseResponseMessage.getIsActive())
                .city(warehouseResponseMessage.getCity())
                .latitude(warehouseResponseMessage.getLatitude())
                .longitude(warehouseResponseMessage.getLongitude())
                .build();
    }

    public User userResponseMessageToUser(UserResponseMessage userResponseMessage) {
        return User.builder()
                .userId(new UserId(UUID.fromString(userResponseMessage.getUserId())))
                .email(userResponseMessage.getEmail())
                .name(userResponseMessage.getName())
                .role(UserRole.valueOf(userResponseMessage.getRole()))
                .token(userResponseMessage.getToken())
                .isActive(userResponseMessage.isActive())
                .build();
    }

    public Product productResponseMessageToProduct(ProductResponseMessage productResponseMessage) {
        return new Product(
                new ProductId(UUID.fromString(productResponseMessage.getProductId())),
                productResponseMessage.getName(),
                productResponseMessage.getPrice()
        );
    }

    public Warehouse stockCreatedResponseMessageToWarehouse(StockCreatedResponseMessage stockCreatedResponseMessage) {
        return Warehouse.builder()
                .warehouseId(new WarehouseId(UUID.fromString(stockCreatedResponseMessage.getWarehouseId())))
                .name(stockCreatedResponseMessage.getWarehouseName())
                .latitude(stockCreatedResponseMessage.getWarehouseLatitude())
                .longitude(stockCreatedResponseMessage.getWarehouseLongitude())
                .products(List.of(
                        new Product(
                                new ProductId(UUID.fromString(stockCreatedResponseMessage.getProductId())),
                                stockCreatedResponseMessage.getProductName(),
                                null
                        )
                ))
                .build();
    }

    public Stock stockCreatedResponseMessageToStock(StockCreatedResponseMessage stockCreatedResponseMessage) {
        return new Stock(
                new StockId(UUID.fromString(stockCreatedResponseMessage.getStockId())),
                new WarehouseId(UUID.fromString(stockCreatedResponseMessage.getWarehouseId())),
                new ProductId(UUID.fromString(stockCreatedResponseMessage.getProductId())),
                stockCreatedResponseMessage.getStock()
        );
    }

    public Stock stockUpdatedResponseMessageToStock(StockUpdatedResponseMessage stockUpdatedResponseMessage) {
        return new Stock(
                new StockId(UUID.fromString(stockUpdatedResponseMessage.getStockId())),
                new WarehouseId(UUID.fromString(stockUpdatedResponseMessage.getWarehouseId())),
                new ProductId(UUID.fromString(stockUpdatedResponseMessage.getProductId())),
                stockUpdatedResponseMessage.getStock()
        );
    }

    public OrderResponse orderToOrderResponse(Order order, Warehouse warehouse,String message) {
        OrderResponse orderResponse = OrderResponse.builder()
                .orderTrackingId(order.getTrackingId().getValue())
                .orderId(order.getId().getValue())
                .orderStatus(order.getOrderStatus())
                .orderAddress(streetAddressToOrderAddress(order.getDeliveryAddress()))
                .totalAmount(order.getPrice().getAmount())
                .warehouse(warehouseToWarehouseResponse(warehouse))
                .items(orderItemsToOrderItemResponse(order.getItems()))
                .message(message)
                .build();
        return orderResponse;

    }

    public OrderAddress streetAddressToOrderAddress(StreetAddress streetAddress) {
        return OrderAddress.builder()
                .street(streetAddress.getStreet())
                .postalCode(streetAddress.getPostalCode())
                .city(streetAddress.getCity())
                .latitude(streetAddress.getLatitude())
                .longitude(streetAddress.getLongitude())
                .build();
    }

    private OrderResponse.WarehouseRespone warehouseToWarehouseResponse(Warehouse warehouse) {
        return OrderResponse.WarehouseRespone.builder()
                .name(warehouse.getName())
                .city(warehouse.getCity())
                .latitude(warehouse.getLatitude())
                .longitude(warehouse.getLongitude())
                .build();
    }

    public List<OrderResponse.OrderItemResponse> orderItemsToOrderItemResponse(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> OrderResponse.OrderItemResponse.builder()
                        .product(productToProductResponse(orderItem.getProduct()))
                        .quantity(orderItem.getQuantity())
                        .subTotal(orderItem.getSubTotal().getAmount())
                        .build())
                .collect(Collectors.toList());
    }

    private OrderResponse.ProductResponse productToProductResponse(Product product) {
        return OrderResponse.ProductResponse.builder()
                .productId(product.getId().getValue().toString())
                .name(product.getName())
                .price(product.getPrice())
                .build();
    }

    public OrderListResponse ordersToOrderListResponse(Page<Order> orderPage) {
        List<OrderResponse> orderResponses = orderPage.getContent().stream()
                .map(order -> orderToOrderResponse(order, order.getWarehouse(), null))
                .collect(Collectors.toList());

        return OrderListResponse.builder()
                .orders(orderResponses)
                .currentPage(orderPage.getNumber())
                .totalPages(orderPage.getTotalPages())
                .totalItems(orderPage.getTotalElements())
                .build();
    }

}
