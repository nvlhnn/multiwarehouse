package com.nvlhnn.warehouse.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.nvlhnn.warehouse.service.dataaccess", "com.nvlhnn.dataaccess" })
@EntityScan(basePackages = { "com.nvlhnn.warehouse.service.dataaccess", "com.nvlhnn.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.nvlhnn")
public class WarehouseServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseServiceApplication.class, args);
    }
}