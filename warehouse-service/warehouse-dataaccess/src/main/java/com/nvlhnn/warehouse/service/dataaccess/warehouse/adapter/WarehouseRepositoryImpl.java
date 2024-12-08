package com.nvlhnn.warehouse.service.dataaccess.warehouse.adapter;

import com.nvlhnn.domain.valueobject.WarehouseId;
import com.nvlhnn.warehouse.service.dataaccess.warehouse.entity.WarehouseEntity;
import com.nvlhnn.warehouse.service.dataaccess.warehouse.mapper.WarehouseDataAccessMapper;
import com.nvlhnn.warehouse.service.dataaccess.warehouse.repository.WarehouseJpaRepository;
import com.nvlhnn.warehouse.service.domain.entity.Warehouse;
import com.nvlhnn.warehouse.service.domain.ports.output.repository.WarehouseRepository;
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
    public Warehouse save(Warehouse warehouse){
        return warehouseDataAccessMapper.warehouseEntityToWarehouse(warehouseJpaRepository
                .save(warehouseDataAccessMapper.warehouseToWarehouseDataMapper(warehouse)));
    }

    @Override
    public Optional<Warehouse> findById(WarehouseId warehouseId){
        Optional<WarehouseEntity> warehouseEntity = warehouseJpaRepository.findById(warehouseId.getValue());

        return warehouseJpaRepository.findById(warehouseId.getValue()).map(warehouseDataAccessMapper::warehouseEntityToWarehouse);
    }


    @Override
    public List<Warehouse> findAll(){
        return warehouseJpaRepository.findAll().stream()
                .map(warehouseDataAccessMapper::warehouseEntityToWarehouse)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Warehouse warehouse){
        warehouseJpaRepository.delete(
                warehouseDataAccessMapper.warehouseToWarehouseDataMapper(warehouse)
        );
    }}
