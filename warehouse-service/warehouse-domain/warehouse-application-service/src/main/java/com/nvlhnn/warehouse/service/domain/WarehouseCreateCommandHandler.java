package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.create.CreateWarehouseCommand;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateWarehouseResponse;
import com.nvlhnn.warehouse.service.domain.event.WarehouseCreatedEvent;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.WarehouseCreatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarehouseCreateCommandHandler {

    private final WarehouseCreateHelper warehouseCreateHelper;
    private final WarehouseDataMapper warehouseDataMapper;
    private final WarehouseCreatedEventPublisher warehouseCreatedEventPublisher;

    public WarehouseCreateCommandHandler(WarehouseCreateHelper warehouseCreateHelper,
                                         WarehouseDataMapper warehouseDataMapper,
                                         WarehouseCreatedEventPublisher warehouseCreatedEventPublisher) {
        this.warehouseCreateHelper = warehouseCreateHelper;
        this.warehouseDataMapper = warehouseDataMapper;
        this.warehouseCreatedEventPublisher = warehouseCreatedEventPublisher;
    }

    public CreateWarehouseResponse createWarehouse(CreateWarehouseCommand createWarehouseCommand) {
        WarehouseCreatedEvent warehouseCreatedEvent = warehouseCreateHelper.persistWarehouse(createWarehouseCommand);
        log.info("Warehouse is created with id: {}", warehouseCreatedEvent.getWarehouse().getId().getValue());
        warehouseCreatedEventPublisher.publish(warehouseCreatedEvent);
        return warehouseDataMapper.warehouseToCreateWarehouseResponse(warehouseCreatedEvent.getWarehouse(),
                "Warehouse created successfully");
    }
}
