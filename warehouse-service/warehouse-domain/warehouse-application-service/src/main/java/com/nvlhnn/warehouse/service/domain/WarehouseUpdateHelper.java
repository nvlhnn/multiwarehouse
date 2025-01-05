package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.dto.create.CreateUpdateWarehouseCommand;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.event.WarehouseUpdatedEvent;
import com.nvlhnn.warehouse.service.domain.exception.WarehouseDomainException;
import com.nvlhnn.warehouse.service.domain.mapper.WarehouseDataMapper;
import com.nvlhnn.warehouse.service.domain.ports.output.message.publisher.WarehouseUpdatedEventPublisher;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.WarehouseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Slf4j
@Component
public class WarehouseUpdateHelper {

    private final WarehouseDomainService warehouseDomainService;
    private final WarehouseRepository warehouseRepository;
    private final WarehouseDataMapper warehouseDataMapper;
    private final WarehouseUpdatedEventPublisher warehouseUpdatedEventPublisher;

    public WarehouseUpdateHelper(WarehouseDomainService warehouseDomainService,
                                 WarehouseRepository warehouseRepository,
                                 WarehouseDataMapper warehouseDataMapper,
                                 WarehouseUpdatedEventPublisher warehouseUpdatedEventPublisher) {
        this.warehouseDomainService = warehouseDomainService;
        this.warehouseRepository = warehouseRepository;
        this.warehouseDataMapper = warehouseDataMapper;
        this.warehouseUpdatedEventPublisher = warehouseUpdatedEventPublisher;
    }

    @Transactional
    public WarehouseUpdatedEvent processWarehouseUpdate(CreateUpdateWarehouseCommand updateWarehouseCommand) {
        Warehouse warehouse = getWarehouseById(updateWarehouseCommand.getWarehouseId());
        WarehouseUpdatedEvent warehouseUpdatedEvent = warehouseDomainService.validateAndPatchWarehouse(
                warehouse,
                updateWarehouseCommand.getName(),
                warehouseDataMapper.warehouseAddressToStreetAddress(updateWarehouseCommand.getWarehouseAddress()),
                warehouseUpdatedEventPublisher);

        saveWarehouse(warehouse);
        return warehouseUpdatedEvent;
    }

    private Warehouse getWarehouseById(UUID warehouseId) {
        return warehouseRepository.findById(new WarehouseId(warehouseId)).orElseThrow(() ->
                new WarehouseDomainException("Warehouse with id " + warehouseId + " not found"));
    }

    private void saveWarehouse(Warehouse warehouse) {
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        if (savedWarehouse == null) {
            log.error("Failed to save updated warehouse with id: {}", warehouse.getId().getValue());
            throw new WarehouseDomainException("Could not save warehouse update!");
        }
        log.info("Updated warehouse is saved with id: {}", savedWarehouse.getId().getValue());
    }
}
