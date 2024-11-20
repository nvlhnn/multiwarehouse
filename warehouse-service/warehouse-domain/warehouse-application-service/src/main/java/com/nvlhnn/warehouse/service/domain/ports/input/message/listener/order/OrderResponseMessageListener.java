package com.nvlhnn.warehouse.service.domain.ports.input.message.listener.order;


import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;

public interface OrderResponseMessageListener {

    void onOrderCreated(OrderResponse orderCreatedResponse);
//    void onOrderCancelled(OrderResponse orderCancelledResponse);

}
