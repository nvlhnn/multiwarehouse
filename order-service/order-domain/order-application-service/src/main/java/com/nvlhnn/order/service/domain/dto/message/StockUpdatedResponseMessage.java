package com.nvlhnn.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class StockUpdatedResponseMessage {

    private final String id;
    private final String sagaId;
    private final String stockId;
    private final String warehouseId;
    private final String productId;
    private final int stock;
    private final Instant eventTimestamp;
}
