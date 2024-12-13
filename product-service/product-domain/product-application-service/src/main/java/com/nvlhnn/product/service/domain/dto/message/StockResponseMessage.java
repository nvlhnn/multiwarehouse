package com.nvlhnn.product.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class StockResponseMessage {

    private final String id;
    private final String sagaId;
    private final String productId;
    private final int totalStockQuantity;
    private final Instant eventTimestamp;

}
