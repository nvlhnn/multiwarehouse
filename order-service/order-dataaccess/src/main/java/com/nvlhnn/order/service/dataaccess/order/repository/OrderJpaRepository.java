package com.nvlhnn.order.service.dataaccess.order.repository;

import com.nvlhnn.domain.valueobject.OrderStatus;
import com.nvlhnn.order.service.dataaccess.order.entity.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID> {

    Optional<OrderEntity> findByTrackingId(UUID trackingId);

    @EntityGraph(attributePaths = {"items.product", "warehouse", "address"})
    @Override
    Page<OrderEntity> findAll(Pageable pageable);

//    find by id
    @EntityGraph(attributePaths = {"items.product", "warehouse", "address"})
    Optional<OrderEntity> findById(UUID id);

    @Query("SELECT o FROM OrderEntity o JOIN FETCH o.items WHERE o.id = :id")
    Optional<OrderEntity> findByIdWithItems(@Param("id") UUID id);

    @EntityGraph(attributePaths = {"items.product", "warehouse", "address"})
    Page<OrderEntity> findByUserId(UUID userId, Pageable pageable);

    @Query(value = "SELECT DATE(o.created_at) AS orderDate, COUNT(o.id) AS totalOrders " +
            "FROM \"order\".orders o " +
            "WHERE o.created_at BETWEEN :startDate AND :endDate " +
            "GROUP BY DATE(o.created_at) " +
            "ORDER BY DATE(o.created_at)", nativeQuery = true)
    List<Object[]> findTotalOrdersByDay(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Modifying
    @Transactional
    @Query("UPDATE OrderEntity o SET o.orderStatus = :orderStatus WHERE o.id = :orderId")
    int payOrder(@Param("orderId") UUID orderId, @Param("orderStatus") OrderStatus orderStatus);

    @Modifying
    @Transactional
    @Query("UPDATE OrderEntity o SET o.orderStatus = :orderStatus WHERE o.id = :orderId")
    int cancelOrder(@Param("orderId") UUID orderId, @Param("orderStatus") OrderStatus orderStatus);

    @Query("SELECT o FROM OrderEntity o WHERE o.expiredAt < :currentDate AND o.orderStatus = :orderStatus")
    Optional<List<OrderEntity>> findExpiredOrders(@Param("currentDate") Date currentDate, @Param("orderStatus") OrderStatus orderStatus);
}
