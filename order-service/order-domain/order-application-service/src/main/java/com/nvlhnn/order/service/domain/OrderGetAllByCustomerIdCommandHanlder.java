package com.nvlhnn.order.service.domain;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.order.service.domain.dto.create.OrderListResponse;
import com.nvlhnn.order.service.domain.entity.User;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.output.repository.OrderRepository;
import com.nvlhnn.order.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Component
public class OrderGetAllByCustomerIdCommandHanlder {
    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;
    private final UserRepository userRepository;

    public OrderGetAllByCustomerIdCommandHanlder(OrderRepository orderRepository,
                                                 UserRepository userRepository,
                                                 OrderDataMapper orderDataMapper) {
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
        this.userRepository = userRepository;
    }

    public OrderListResponse getAllByCustomerId(String userId, int page, int size) {

        Optional<User> user = userRepository.findById(new UserId(UUID.fromString(userId)));
        if (user.isEmpty()) {
            new OrderDomainException("Could not find user with user id: " + userId);
        }

        log.info("Listing orders for page: {} with size: {}", page, size);
        return orderDataMapper.ordersToOrderListResponse(
                orderRepository.findByUserId(user.get().getId().getValue(), (PageRequest.of(page, size))));
    }
}
