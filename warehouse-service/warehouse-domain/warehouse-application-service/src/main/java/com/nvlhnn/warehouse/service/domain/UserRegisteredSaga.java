package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.dto.message.UserResponseMessage;
import com.nvlhnn.warehouse.service.domain.entity.Stock;
import com.nvlhnn.warehouse.service.domain.entity.User;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.StockUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class UserRegisteredSaga {

    private final WarehouseDomainService warehouseDomainService;
    private final UserRepository userRepository;
    private final WarehouseDataMapper warehouseDataMapper;

    public UserRegisteredSaga(WarehouseDomainService warehouseDomainService, UserRepository userRepository, WarehouseDataMapper warehouseDataMapper) {
        this.warehouseDomainService = warehouseDomainService;
        this.userRepository = userRepository;
        this.warehouseDataMapper = warehouseDataMapper;
    }

    @Transactional
    public void process(UserResponseMessage userResponseMessage) {
        log.info("Processing insert user: {}", userResponseMessage.getUserId());

        User user = warehouseDataMapper.userResponseMessageToUser(userResponseMessage);
        warehouseDomainService.createUser(user);

        userRepository.save(user);
        log.info("User is saved with email: {}", user.getEmail());
    }
}
