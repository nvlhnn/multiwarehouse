package com.nvlhnn.warehouse.service.domain.dto.message;

import com.nvlhnn.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class PaymentResponse {
    private final String sagaId;
    private final String orderId;
    private final OrderStatus status;

}
