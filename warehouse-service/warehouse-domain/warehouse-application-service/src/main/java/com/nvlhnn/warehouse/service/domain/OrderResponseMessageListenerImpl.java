package com.nvlhnn.warehouse.service.domain;

import com.nvlhnn.warehouse.service.domain.dto.message.OrderResponse;
import com.nvlhnn.warehouse.service.domain.dto.message.PaymentResponse;
import com.nvlhnn.warehouse.service.domain.ports.input.message.listener.order.OrderResponseMessageListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderResponseMessageListenerImpl implements OrderResponseMessageListener {

    private final OrderStockUpdateSaga orderStockUpdateSaga;
    private final OrderPaidSaga orderPaidSaga;
    private final OrderCancelledSaga orderCancelledSaga;

    public OrderResponseMessageListenerImpl(OrderStockUpdateSaga orderStockUpdateSaga, OrderPaidSaga orderPaidSaga, OrderCancelledSaga orderCancelledSaga) {
        this.orderStockUpdateSaga = orderStockUpdateSaga;
        this.orderPaidSaga = orderPaidSaga;
        this.orderCancelledSaga = orderCancelledSaga;
    }

    @Override
    public void onOrderCreated(OrderResponse orderResponse) {
        log.info("Received OrderCreated event for order id: {}", orderResponse.getOrderId());
        orderStockUpdateSaga.process(orderResponse);
    }

    @Override
    public void onOrderPaid(PaymentResponse paymentResponse) {
        log.info("Received OrderPaid event for order id: {}", paymentResponse.getOrderId());
        orderPaidSaga.process(paymentResponse);
    }

    @Override
    public void onOrderCancelled(PaymentResponse paymentResponse) {
        log.info("Received OrderCancelled event for order id: {}", paymentResponse.getOrderId());
        orderCancelledSaga.process(paymentResponse);
    }

}
