package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.order.service.domain.entity.User;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Component
public class UserRegisteredSaga {

    private final OrderDomainService orderDomainService;
    private final UserRepository userRepository;
    private final OrderDataMapper orderDataMapper;

    public UserRegisteredSaga(OrderDomainService orderDomainService, UserRepository userRepository, OrderDataMapper orderDataMapper) {
        this.orderDomainService = orderDomainService;
        this.userRepository = userRepository;
        this.orderDataMapper = orderDataMapper;
    }

    @Transactional
    public void process(UserResponseMessage userResponseMessage) {
        log.info("Processing insert user: {}", userResponseMessage.getUserId());

        User user = orderDataMapper.userResponseMessageToUser(userResponseMessage);
        orderDomainService.createUser(user);

        userRepository.save(user);
        log.info("User is saved with email: {}", user.getEmail());
    }
}
