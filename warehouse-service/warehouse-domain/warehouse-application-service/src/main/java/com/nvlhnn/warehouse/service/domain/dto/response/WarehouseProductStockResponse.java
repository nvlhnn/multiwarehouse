package com.nvlhnn.warehouse.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;


@AllArgsConstructor
@Getter
@Builder
public class WarehouseProductStockResponse {
    @NotNull
    private final UUID warehouseId;

    @NotNull
    private final UUID productId;

    @NotNull
    private final Integer quantity;
}
