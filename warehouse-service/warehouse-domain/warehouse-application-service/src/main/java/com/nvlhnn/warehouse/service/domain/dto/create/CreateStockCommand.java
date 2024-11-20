package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CreateStockCommand {

    @NotNull
    private final UUID warehouseId;

    @NotNull
    private final UUID productId;

    @NotNull
    @Min(1)
    private final int quantity;


}
