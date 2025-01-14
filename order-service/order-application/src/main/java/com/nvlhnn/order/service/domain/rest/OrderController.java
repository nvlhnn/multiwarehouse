package com.nvlhnn.order.service.domain.rest;

import com.nvlhnn.domain.valueobject.UserId;
import com.nvlhnn.order.service.domain.dto.create.*;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderQuery;
import com.nvlhnn.order.service.domain.dto.track.TrackOrderResponse;
import com.nvlhnn.order.service.domain.ports.input.service.OrderApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.http.HttpRequest;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping(value = "/orders", produces = "application/vnd.api.v1+json")
public class OrderController {

    private final OrderApplicationService orderApplicationService;

    public OrderController(OrderApplicationService orderApplicationService) {
        this.orderApplicationService = orderApplicationService;
    }

    @PostMapping
    public ResponseEntity<CreateOrderResponse> createOrder(@RequestBody CreateOrderCommand createOrderCommand, HttpServletRequest request) {
        log.info("Creating order");

        UUID userId =  UUID.fromString(request.getAttribute("userId").toString());
        createOrderCommand.setUserId(userId);

        CreateOrderResponse createOrderResponse = orderApplicationService.createOrder(createOrderCommand);
        log.info("Order created with tracking id: {}", createOrderResponse.getOrderTrackingId());
        return ResponseEntity.ok(createOrderResponse);
    }

    @GetMapping("/{trackingId}")
    public ResponseEntity<TrackOrderResponse> getOrderByTrackingId(@PathVariable UUID trackingId) {
        TrackOrderResponse trackOrderResponse =
                orderApplicationService.trackOrder(TrackOrderQuery.builder().orderTrackingId(trackingId).build());
        log.info("Returning order status with tracking id: {}", trackOrderResponse.getOrderTrackingId());
        return  ResponseEntity.ok(trackOrderResponse);
    }

    @GetMapping
    public ResponseEntity<OrderListResponse> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        OrderListResponse response = orderApplicationService.getAllOrders(page, size);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/stats")
    public ResponseEntity<OrderStatsResponse> getStat() {
        OrderStatsResponse response = orderApplicationService.getLastWeekOrderStat();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/customers")
    public ResponseEntity<OrderListResponse> getCustomerOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            HttpServletRequest request) {

        String customerId = request.getAttribute("userId").toString();
        if (customerId == null) {
            return ResponseEntity.badRequest().build();
        }

        OrderListResponse response = orderApplicationService.getAllOrdersByCustomerId(customerId, page, size);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pay")
    public ResponseEntity<OrderResponse> payment(@RequestBody OrderPaidCommand orderPaidCommand) {

        String orderId = orderPaidCommand.getExternalId();
        if (orderId == null) {
            return ResponseEntity.badRequest().build();
        }

        OrderResponse orderResponse = orderApplicationService.payment(orderId);
        return ResponseEntity.ok(orderResponse);
    }

    @PostMapping("/expiring")
    public ResponseEntity<Object> cancel() {
        orderApplicationService.cancel();
        return ResponseEntity.ok().build();
    }
}
