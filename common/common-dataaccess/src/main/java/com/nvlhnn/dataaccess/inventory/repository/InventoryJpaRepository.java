package com.nvlhnn.dataaccess.inventory.repository;

import com.nvlhnn.dataaccess.inventory.entity.InventoryEntity;
import com.nvlhnn.dataaccess.inventory.entity.InventoryEntityId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InventoryJpaRepository extends JpaRepository<InventoryEntity, InventoryEntityId> {

//    Optional< List<InventoryEntity> > findByWarehouseIdAndProductIdIn(UUID warehouseId, List<UUID> productIds);
}
