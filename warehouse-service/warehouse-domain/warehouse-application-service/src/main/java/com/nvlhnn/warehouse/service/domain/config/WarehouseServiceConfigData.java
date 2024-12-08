package com.nvlhnn.warehouse.service.domain.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "warehouse-service")
public class WarehouseServiceConfigData {
    private String warehouseCreatedTopicName;
    private String warehouseUpdatedTopicName;
    private String warehouseStockUpdatedTopicName;
    private String orderResponseTopicName;
    private String userRegisteredTopicName;
    private String productCreatedTopicName;

}
