package com.nvlhnn.product.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class ProductResponse {

    private final UUID productId;

    private final String name;

    private final BigDecimal price;

    private final Integer totalStock;

    private final String imageUrl;

    private final String message;
}
