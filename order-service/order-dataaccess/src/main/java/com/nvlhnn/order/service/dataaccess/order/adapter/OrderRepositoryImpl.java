package com.nvlhnn.order.service.dataaccess.order.adapter;

import com.nvlhnn.domain.valueobject.OrderId;
import com.nvlhnn.domain.valueobject.OrderStatus;
import com.nvlhnn.order.service.dataaccess.order.mapper.OrderDataAccessMapper;
import com.nvlhnn.order.service.dataaccess.order.repository.OrderJpaRepository;
import com.nvlhnn.order.service.domain.entity.Order;
import com.nvlhnn.order.service.domain.exception.OrderDomainException;
import com.nvlhnn.order.service.domain.ports.output.repository.OrderRepository;
import com.nvlhnn.order.service.domain.valueobject.TrackingId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;
    private final OrderDataAccessMapper orderDataAccessMapper;

    public OrderRepositoryImpl(OrderJpaRepository orderJpaRepository,
                               OrderDataAccessMapper orderDataAccessMapper) {
        this.orderJpaRepository = orderJpaRepository;
        this.orderDataAccessMapper = orderDataAccessMapper;
    }

    @Override
    public Order save(Order order) {
        return orderDataAccessMapper.orderEntityToOrder(orderJpaRepository
                .save(orderDataAccessMapper.orderToOrderEntity(order)));
    }

    @Override
    public Optional<Order> findById(OrderId orderId) {
        return orderJpaRepository.findById(orderId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findByIdWithItems(OrderId orderId) {
        return orderJpaRepository.findByIdWithItems(orderId.getValue()).map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Optional<Order> findByTrackingId(TrackingId trackingId) {
        return orderJpaRepository.findByTrackingId(trackingId.getValue())
                .map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderJpaRepository.findAll(pageable).map(orderDataAccessMapper::orderEntityToOrder);
    }


    @Override
    public Page<Order> findByUserId(UUID userId, Pageable pageable) {
        return orderJpaRepository.findByUserId(userId, pageable).map(orderDataAccessMapper::orderEntityToOrder);
    }

    @Override
    public List<Object[]> findTotalOrdersByDay(LocalDate startDate, LocalDate endDate) {
        return orderJpaRepository.findTotalOrdersByDay(startDate, endDate);
    }

    @Override
    public int payOrder(Order order) {
        return orderJpaRepository.payOrder(order.getId().getValue(), order.getOrderStatus());

    }

    @Override
    public int cancelOrder(Order order) {
        return orderJpaRepository.cancelOrder(order.getId().getValue(), order.getOrderStatus());
    }

    @Override
    public Optional<List<Order>> findExpiredOrders() {
        Date now = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        return orderJpaRepository.findExpiredOrders(now, OrderStatus.PENDING).map(orderEntities -> orderEntities.stream()
                .map(orderDataAccessMapper::orderEntityToOrderExpiring)
                .collect(Collectors.toList()));
    }

}