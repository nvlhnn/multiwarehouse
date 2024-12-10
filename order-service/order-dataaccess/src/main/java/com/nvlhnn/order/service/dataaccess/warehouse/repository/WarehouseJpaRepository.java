package com.nvlhnn.order.service.dataaccess.warehouse.repository;

import com.nvlhnn.order.service.dataaccess.warehouse.entity.WarehouseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WarehouseJpaRepository extends JpaRepository<WarehouseEntity, UUID> {

    @Query(value = "SELECT * FROM warehouses " +
            "ORDER BY (6371 * ACOS(COS(RADIANS(:latitude)) * COS(RADIANS(latitude)) * COS(RADIANS(longitude) - RADIANS(:longitude)) + SIN(RADIANS(:latitude)) * SIN(RADIANS(latitude)))) ASC " +
            "LIMIT 1", nativeQuery = true)
    Optional<WarehouseEntity> findNearestLocation(double latitude, double longitude);


}