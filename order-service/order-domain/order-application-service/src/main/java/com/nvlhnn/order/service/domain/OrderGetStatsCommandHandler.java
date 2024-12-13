package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.dto.create.OrderStatsResponse;
import com.nvlhnn.order.service.domain.mapper.OrderDataMapper;
import com.nvlhnn.order.service.domain.ports.output.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Slf4j
@Component
public class OrderGetStatsCommandHandler {

    private final OrderRepository orderRepository;
    private final OrderDataMapper orderDataMapper;

    public OrderGetStatsCommandHandler(OrderRepository orderRepository, OrderDataMapper orderDataMapper) {
        this.orderRepository = orderRepository;
        this.orderDataMapper = orderDataMapper;
    }

    public OrderStatsResponse getOrderStats() {
        LocalDate today = LocalDate.now().plusDays(1);
        LocalDate sevenDaysAgo = today.minusDays(7);

        List<Object[]> rawStats = orderRepository.findTotalOrdersByDay(sevenDaysAgo, today);

        // Convert raw stats to a map for easier lookup
        Map<LocalDate, Long> statsMap = rawStats.stream()
                .collect(Collectors.toMap(
                        row -> ((java.sql.Date) row[0]).toLocalDate(),
                        row -> ((BigInteger) row[1]).longValue() // Convert BigInteger to Long
                ));

        // Generate a complete list of daily stats for the last 7 days
        List<OrderStatsResponse.DailyStats> dailyStats = IntStream.rangeClosed(0, 6)
                .mapToObj(i -> today.minusDays(7 - i))
                .map(date -> OrderStatsResponse.DailyStats.builder()
                        .date(date.toString())
                        .totalOrders(statsMap.getOrDefault(date, 0L))
                        .build())
                .collect(Collectors.toList());

        return OrderStatsResponse.builder()
                .message("Order statistics for the last seven days")
                .dailyStats(dailyStats)
                .build();
    }
}
