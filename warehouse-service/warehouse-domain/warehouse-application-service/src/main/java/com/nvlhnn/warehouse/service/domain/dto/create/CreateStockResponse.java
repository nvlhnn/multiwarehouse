package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CreateStockResponse {

    private UUID stockId;
    private UUID warehouseId;
    private UUID productId;
    private String message;


}
