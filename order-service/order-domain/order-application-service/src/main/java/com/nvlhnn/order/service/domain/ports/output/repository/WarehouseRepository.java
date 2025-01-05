package com.nvlhnn.order.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.domain.entity.Warehouse;

import java.util.Optional;

public interface WarehouseRepository {
    Optional<Warehouse> findById(WarehouseId warehouseId);

    void save(Warehouse warehouse);

    Optional<Warehouse> findNearestLocation(double latitude, double longitude);
}
