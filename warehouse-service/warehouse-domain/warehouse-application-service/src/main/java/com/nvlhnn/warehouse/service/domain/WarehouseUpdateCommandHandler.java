package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.create.CreateUpdateWarehouseCommand;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateWarehouseResponse;
import com.nvlhnn.warehouse.service.domain.event.WarehouseUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.WarehouseUpdatedEventPublisher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WarehouseUpdateCommandHandler {

    private final WarehouseUpdateHelper warehouseUpdateHelper;
    private final WarehouseDataMapper warehouseDataMapper;
    private final WarehouseUpdatedEventPublisher warehouseUpdatedEventPublisher;

    public WarehouseUpdateCommandHandler(WarehouseUpdateHelper warehouseUpdateHelper,
                                         WarehouseDataMapper warehouseDataMapper,
                                         WarehouseUpdatedEventPublisher warehouseUpdatedEventPublisher) {
        this.warehouseUpdateHelper = warehouseUpdateHelper;
        this.warehouseDataMapper = warehouseDataMapper;
        this.warehouseUpdatedEventPublisher = warehouseUpdatedEventPublisher;
    }

    public CreateWarehouseResponse updateWarehouse(CreateUpdateWarehouseCommand updateWarehouseCommand) {
        WarehouseUpdatedEvent warehouseUpdatedEvent = warehouseUpdateHelper.processWarehouseUpdate(updateWarehouseCommand);
        log.info("Warehouse with id: {} is updated successfully", warehouseUpdatedEvent.getWarehouse().getId().getValue());
        warehouseUpdatedEventPublisher.publish(warehouseUpdatedEvent);
        return warehouseDataMapper.warehouseToCreateWarehouseResponse(warehouseUpdatedEvent.getWarehouse(), "Warehouse updated successfully");
    }
}
