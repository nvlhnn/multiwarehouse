package com.nvlhnn.order.service.domain;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.order.service.domain.dto.create.OrderListResponse;
import com.nvlhnn.order.service.domain.dto.create.OrderResponse;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class OrderPayment {

    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;
    private final OrderDomainService orderDomainService;

    public OrderPayment(OrderRepository orderRepository, OrderDataMapper orderDataMapper, OrderDomainService orderDomainService) {
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
        this.orderDomainService = orderDomainService;
    }

    public OrderResponse orderPayment(String orderId) {
        log.info("Processing order payment for order id: {}", orderId);
        Order order = orderRepository.findById(new OrderId(UUID.fromString(orderId)))
                .orElseThrow(() -> new OrderDomainException("Could not find order with id: " + orderId));

        orderDomainService.payOrder(order);

        int success = orderRepository.payOrder(order);
        if (success == 0) {
            throw new OrderDomainException("Failed to update order status for order id: " + orderId);
        }

        log.info("Order payment is done for order id: {}", orderId);
        return orderDataMapper.orderToOrderResponse(order, order.getWarehouse(), "Order payment done successfully");
    }
}
