package com.nvlhnn.order.service.domain.dto.create;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class OrderStatsResponse {

    private final String message;
    private final List<DailyStats> dailyStats;

    @Getter
    @AllArgsConstructor
    @Builder
    public static class DailyStats {
        private final String date;
        private final long totalOrders;
    }
}
