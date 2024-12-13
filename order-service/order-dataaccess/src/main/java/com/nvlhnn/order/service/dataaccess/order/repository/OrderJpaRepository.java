package com.nvlhnn.order.service.dataaccess.order.repository;

import com.nvlhnn.order.service.dataaccess.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);

    @EntityGraph(attributePaths = {"items.product", "warehouse", "address"})
    @Override
    Page<OrderEntity> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"items.product", "warehouse", "address"})
    Page<OrderEntity> findByUserId(UUID userId, Pageable pageable);

    @Query(value = "SELECT DATE(o.created_at) AS orderDate, COUNT(o.id) AS totalOrders " +
            "FROM orders o " +
            "WHERE o.created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.created_at) " +
            "ORDER BY DATE(o.created_at)", nativeQuery = true)
    List<Object[]> findTotalOrdersByDay(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);



}
