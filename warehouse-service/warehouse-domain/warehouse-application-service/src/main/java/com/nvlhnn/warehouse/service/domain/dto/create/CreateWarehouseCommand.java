package com.nvlhnn.warehouse.service.domain.dto.create;

import com.nvlhnn.warehouse.service.domain.valueobject.StreetAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@Getter
@Builder
public class CreateWarehouseCommand {

    @NotNull
    private final String name;

    @NotNull
    private final WarehouseAddress warehouseAddress;

}
