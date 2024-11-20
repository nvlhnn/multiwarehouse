package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CreateTransferStockResponse {

    @NotNull
    private UUID productId;

    @NotNull
    private int fromWarehouseQuantity;

    @NotNull
    private int toWarehouseQuantity;

    @NotNull
    private String message;
}
