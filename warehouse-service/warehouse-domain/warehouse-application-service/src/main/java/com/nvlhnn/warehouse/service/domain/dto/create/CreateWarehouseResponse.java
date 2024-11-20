package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CreateWarehouseResponse {
    @NotNull
    private final UUID warehouseId;

    @NotNull
    private final boolean isActive;

    @NotNull
    private final String message;
}
