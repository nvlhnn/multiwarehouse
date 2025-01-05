package com.nvlhnn.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderListResponse {
    private final List<OrderResponse> orders;
    private final int currentPage;
    private final int totalPages;
    private final long totalItems;
}
