package com.nvlhnn.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class StockCreatedResponseMessage {

    private final String id;
    private final String sagaId;
    private final String stockId;
    private final String warehouseId;
    private final String productId;
    private final String warehouseName;
    private final String productName;
    private final double warehouseLatitude;
    private final double warehouseLongitude;
    private final int stock;
    private final Instant eventTimestamp;
}
