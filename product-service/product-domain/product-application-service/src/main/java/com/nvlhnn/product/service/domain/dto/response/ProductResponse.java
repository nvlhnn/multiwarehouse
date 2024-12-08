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

    @NotNull
    private final UUID productId;

    @NotNull
    private final String name;

    @NotNull
    private final BigDecimal price;

    @NotNull
    private final Integer totalStock;

    @NotNull
    private final String message;
}
