package com.nvlhnn.order.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.valueobject.TrackingId;

import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(OrderId orderId);

    Optional<Order> findByTrackingId(TrackingId trackingId);
}
