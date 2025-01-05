package com.nvlhnn.warehouse.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class WarehouseListResponse {
    private final List<WarehouseResponse> warehouses;
    private final long totalElements;
    private final int totalPages;
    private final int currentPage;
}
