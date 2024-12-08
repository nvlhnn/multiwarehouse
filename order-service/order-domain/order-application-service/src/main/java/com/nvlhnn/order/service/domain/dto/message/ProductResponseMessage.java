package com.nvlhnn.order.service.domain.dto.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Builder
@AllArgsConstructor
public class ProductResponseMessage {

    private final String id;
    private final String sagaId;
    private final String productId;
    private final String name;
    private final BigDecimal price;
    private final Instant eventTimestamp;

}
