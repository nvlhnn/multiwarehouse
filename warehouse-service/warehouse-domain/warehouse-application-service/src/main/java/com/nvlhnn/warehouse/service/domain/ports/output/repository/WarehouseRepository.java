package com.nvlhnn.warehouse.service.domain.ports.output.repository;

import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface WarehouseRepository {

    Warehouse save(Warehouse warehouse);

    Optional<Warehouse> findById(WarehouseId warehouseId);

    Page<Warehouse> findAll(Pageable pageable); // Updated for pagination



    void delete(Warehouse warehouse);
}
