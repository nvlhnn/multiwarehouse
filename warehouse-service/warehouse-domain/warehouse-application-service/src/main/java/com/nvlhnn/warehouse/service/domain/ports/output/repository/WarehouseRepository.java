package com.nvlhnn.warehouse.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(WarehouseId warehouseId);

    List<Warehouse> findAll();

    void delete(Warehouse warehouse);
}
