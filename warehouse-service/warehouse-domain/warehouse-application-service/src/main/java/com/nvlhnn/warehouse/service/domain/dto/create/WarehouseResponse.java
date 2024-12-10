package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Builder
public class WarehouseResponse {
    private final UUID warehouseId;
    private final String name;
    private final boolean isActive;
    private final String city;
    private final String street;
    private final String postalCode;
    private final Double latitude;
    private final Double longitude;
}
