package com.nvlhnn.order.service.domain.ports.input.service;

import com.nvlhnn.order.service.domain.dto.create.*;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderQuery;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderResponse;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

public interface OrderApplicationService {
    CreateOrderResponse createOrder(@Valid CreateOrderCommand createOrderCommand, HttpServletRequest request);

    TrackOrderResponse trackOrder(@Valid TrackOrderQuery trackOrderQuery);

//    OrderResponse getAllByCustomerId();

    OrderListResponse getAllOrders(int page, int size);

    OrderListResponse getAllOrdersByCustomerId(String customerId, int page, int size);

    OrderStatsResponse getLastWeekOrderStat();


}
