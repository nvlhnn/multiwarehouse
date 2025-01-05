package com.nvlhnn.warehouse.service.domain.ports.input.message.listener.order;


import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.dto.message.PaymentResponse;

public interface OrderResponseMessageListener {

    void onOrderCreated(OrderResponse orderCreatedResponse);

    void onOrderPaid(PaymentResponse paymentResponse);

    void onOrderCancelled(PaymentResponse paymentResponse);

//    void onOrderCancelled(OrderResponse orderCancelledResponse);

}
