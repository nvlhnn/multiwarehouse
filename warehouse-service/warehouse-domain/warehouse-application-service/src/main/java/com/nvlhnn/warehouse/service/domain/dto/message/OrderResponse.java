package com.nvlhnn.warehouse.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class OrderResponse {
    private final String id;
    private final String sagaId;
    private final String orderId;
    private final String warehoudId;
    private final Instant createdAt;
    private final List<ProductQuantity> products;

    private final Instant eventTimestamp;

    @Getter
    @AllArgsConstructor
    public static class ProductQuantity {
        private final String productId;
        private final String stockId;
        private final int quantity;
    }
}
