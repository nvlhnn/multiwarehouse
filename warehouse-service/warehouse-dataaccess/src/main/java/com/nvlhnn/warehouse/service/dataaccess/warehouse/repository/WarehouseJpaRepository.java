package com.nvlhnn.warehouse.service.dataaccess.warehouse.repository;

import com.nvlhnn.warehouse.service.dataaccess.warehouse.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<WarehouseEntity, UUID> {

}