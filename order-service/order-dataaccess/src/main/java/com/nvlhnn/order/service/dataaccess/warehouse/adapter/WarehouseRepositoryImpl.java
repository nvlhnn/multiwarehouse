package com.nvlhnn.order.service.dataaccess.warehouse.adapter;

import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.order.service.dataaccess.warehouse.mapper.WarehouseDataAccessMapper;
import com.nvlhnn.order.service.dataaccess.warehouse.repository.WarehouseJpaRepository;
import com.nvlhnn.order.service.domain.entity.Warehouse;
import com.nvlhnn.order.service.domain.ports.output.repository.WarehouseRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Component
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final WarehouseJpaRepository warehouseJpaRepository;
    private final WarehouseDataAccessMapper warehouseDataAccessMapper;

    public WarehouseRepositoryImpl(WarehouseJpaRepository warehouseJpaRepository, WarehouseDataAccessMapper warehouseDataAccessMapper){
        this.warehouseJpaRepository = warehouseJpaRepository;
        this.warehouseDataAccessMapper = warehouseDataAccessMapper;
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId warehouseId){
        return warehouseJpaRepository.findById(warehouseId.getValue()).map(warehouseDataAccessMapper::warehouseEntityToWarehouse);
    }


    @Override
    public void save(Warehouse warehouse){
        warehouseJpaRepository.save(warehouseDataAccessMapper.warehouseToWarehouseEntity(warehouse));
    }

}
