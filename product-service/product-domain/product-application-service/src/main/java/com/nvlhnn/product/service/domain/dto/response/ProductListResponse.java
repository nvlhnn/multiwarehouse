package com.nvlhnn.product.service.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
@Builder
public class ProductListResponse {
    private final List<ProductResponse> products;
    private final int totalPages;
    private final long totalElements;
    private final int currentPage;
}
