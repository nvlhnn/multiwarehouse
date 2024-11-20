package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class CreateUpdateWarehouseCommand {

    @NotNull
    private final UUID warehouseId;

    @NotNull
    private final String name;

    @NotNull
    private final WarehouseAddress warehouseAddress;


}
