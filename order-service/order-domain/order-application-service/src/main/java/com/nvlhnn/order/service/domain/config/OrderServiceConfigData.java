package com.nvlhnn.order.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "order-service")
public class OrderServiceConfigData {
    private String paymentResponseTopicName;
    private String orderResponseTopicName;
    private String userRegisteredTopicName;
    private String productCreatedTopicName;
    private String stockCreatedTopicName;
    private String stockUpdatedTopicName;

}