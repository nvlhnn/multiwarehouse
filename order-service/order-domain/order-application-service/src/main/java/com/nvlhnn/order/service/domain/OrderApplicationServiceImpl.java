package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.dto.create.*;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderQuery;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderResponse;
import com.nvlhnn.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;

@Slf4j
@Validated
@Service
class OrderApplicationServiceImpl implements OrderApplicationService {

    private final OrderCreateCommandHandler orderCreateCommandHandler;

    private final OrderTrackCommandHandler orderTrackCommandHandler;

    private final OrderGetAllCommandHandler orderGetAllCommandHandler;

    private final OrderGetAllByCustomerIdCommandHanlder orderGetAllByCustomerIdCommandHandler;

    private final OrderGetStatsCommandHandler orderGetStatsCommandHandler;

    private final OrderPayment orderPayment;

    OrderApplicationServiceImpl(OrderCreateCommandHandler orderCreateCommandHandler,
                                OrderTrackCommandHandler orderTrackCommandHandler,
                                OrderGetAllCommandHandler orderGetAllCommandHandler,
                                OrderGetStatsCommandHandler orderGetStatsCommandHandler,
                                OrderGetAllByCustomerIdCommandHanlder orderGetAllByCustomerIdCommandHandler,
                                OrderPayment orderPayment

    ) {
        this.orderCreateCommandHandler = orderCreateCommandHandler;
        this.orderTrackCommandHandler = orderTrackCommandHandler;
        this.orderGetAllCommandHandler = orderGetAllCommandHandler;
        this.orderGetAllByCustomerIdCommandHandler = orderGetAllByCustomerIdCommandHandler;
        this.orderGetStatsCommandHandler = orderGetStatsCommandHandler;
        this.orderPayment = orderPayment;
    }

    @Override
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        return orderCreateCommandHandler.createOrder(createOrderCommand);
    }

    @Override
    public TrackOrderResponse trackOrder(TrackOrderQuery trackOrderQuery) {
        return orderTrackCommandHandler.trackOrder(trackOrderQuery);
    }

//    @Override
//    public OrderResponse getAllByCustomerId() {
//        return orderGetAllByCustomerIdCommandHandler.getAllByCustomerId();
//    }
//
    @Override
    public OrderListResponse getAllOrders(int page, int size) {
        return orderGetAllCommandHandler.getAllOrders(page, size);
    }

    @Override
    public OrderListResponse getAllOrdersByCustomerId(String customerId, int page, int size) {
        return orderGetAllByCustomerIdCommandHandler.getAllByCustomerId(customerId, page, size);
    }

    @Override
    public OrderStatsResponse getLastWeekOrderStat() {
        return orderGetStatsCommandHandler.getOrderStats();
    }

    @Override
    public OrderResponse payment(String orderId) {
        return orderPayment.orderPayment(orderId);
    }
}
