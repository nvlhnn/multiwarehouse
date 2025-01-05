package com.nvlhnn.warehouse.service.dataaccess.orderStockMutation.repository;

import com.nvlhnn.warehouse.service.dataaccess.orderStockMutation.entity.OrderStockMutationEntity;
import com.nvlhnn.warehouse.service.dataaccess.stock.entity.StockEntity;
import com.nvlhnn.warehouse.service.domain.valueobject.StatusStockMutation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderStockMutationJpaRepository extends JpaRepository<OrderStockMutationEntity, UUID> {

    Optional<OrderStockMutationEntity> findByWarehouseIdAndProductId(UUID warehouseId, UUID productId);

    @Query("UPDATE OrderStockMutationEntity o SET o.statusStockMutation = :statusStockMutation WHERE o.id = :id")
    void updateStatusStockMutation(@Param("id") UUID id, @Param("statusStockMutation") StatusStockMutation statusStockMutation);

    Optional<List<OrderStockMutationEntity>> findByOrderId(UUID orderId);
}
