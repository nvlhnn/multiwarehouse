package com.nvlhnn.warehouse.service.dataaccess.stock.repository;

import com.nvlhnn.warehouse.service.dataaccess.stock.entity.StockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StockJpaRepository extends JpaRepository<StockEntity, UUID> {

    Optional<StockEntity> findByWarehouseIdAndProductId(UUID warehouseId, UUID productId);

    List<StockEntity> findByWarehouseId(UUID warehouseId);

    Optional<List<StockEntity>> findByProductIdIn(List<UUID> productIds);

}
