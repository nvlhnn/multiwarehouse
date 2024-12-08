package com.nvlhnn.warehouse.service.domain.rest;

import com.nvlhnn.warehouse.service.domain.dto.create.*;
import com.nvlhnn.warehouse.service.domain.ports.input.service.WarehouseApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/warehouses", produces = "application/vnd.api.v1+json")
public class WarehouseController {

    private final WarehouseApplicationService warehouseApplicationService;

    public WarehouseController(WarehouseApplicationService warehouseApplicationService) {
        this.warehouseApplicationService = warehouseApplicationService;
    }

    // Create Warehouse
    @PostMapping
    public ResponseEntity<CreateWarehouseResponse> createWarehouse(@RequestBody CreateWarehouseCommand createWarehouseCommand) {
        log.info("Creating warehouse with name of {}", createWarehouseCommand.getName());
        CreateWarehouseResponse createWarehouseResponse = warehouseApplicationService.createWarehouse(createWarehouseCommand);
        log.info("Warehouse created with id: {}", createWarehouseResponse.getWarehouseId().toString());
        return ResponseEntity.ok(createWarehouseResponse);
    }

    @PostMapping("/stocks")
    public ResponseEntity<CreateStockResponse> createStock(@RequestBody @Valid CreateStockCommand createStockCommand) {
        CreateStockResponse createStockResponse = warehouseApplicationService.createStock(createStockCommand);
        log.info("Stock created with ID: {}", createStockResponse.getStockId());
        return ResponseEntity.ok(createStockResponse);
    }

    @PostMapping("/stocks/apply")
    public ResponseEntity<CreateStockResponse> updateStock(@RequestBody @Valid CreateUpdateStockCommand updateStockCommand) {
        CreateStockResponse updateStockResponse = warehouseApplicationService.updateStock(updateStockCommand);
        log.info("Stock applied with ID: {}", updateStockResponse.getStockId());
        return ResponseEntity.ok(updateStockResponse);
    }

//
//    @PutMapping("/{id}")
//    public ResponseEntity<CreateWarehouseResponse> updateWarehouse(
//            @PathVariable UUID id,
//            @RequestBody CreateUpdateWarehouseCommand updateWarehouseCommand) {
//        log.info("Updating warehouse with id: {}", id);
//        CreateUpdateWarehouseCommand command = CreateUpdateWarehouseCommand.builder()
//                .warehouseId(id)
//                .name(updateWarehouseCommand.getName())
//                .warehouseAddress(updateWarehouseCommand.getWarehouseAddress())
//                .build();
//        CreateWarehouseResponse updateWarehouseResponse = warehouseApplicationService.updateWarehouse(command);
//        log.info("Warehouse updated with id: {}", updateWarehouseResponse.getWarehouseId());
//        return ResponseEntity.ok(updateWarehouseResponse);
//    }


}
