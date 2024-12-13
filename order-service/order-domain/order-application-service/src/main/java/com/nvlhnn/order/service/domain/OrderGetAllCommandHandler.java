package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.dto.create.OrderListResponse;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderGetAllCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderGetAllCommandHandler(OrderRepository orderRepository, OrderDataMapper orderDataMapper) {
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
    }

    public OrderListResponse getAllOrders(int page, int size) {
        log.info("Listing orders for page: {} with size: {}", page, size);
        return orderDataMapper.ordersToOrderListResponse(
                orderRepository.findAll(PageRequest.of(page, size)));
    }
}
