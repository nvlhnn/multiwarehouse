package com.nvlhnn.order.service.domain.dto.create;

import com.nvlhnn.domain.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {
    @NotNull
    private final UUID orderTrackingId;
    @NotNull
    private final UUID orderId;
    @NotNull
    private final OrderStatus orderStatus;
    @NotNull
    private final String message;
}
