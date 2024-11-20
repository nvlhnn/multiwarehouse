package com.nvlhnn.order.service.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories(basePackages = { "com.nvlhnn.order.service.dataaccess", "com.nvlhnn.dataaccess" })
@EntityScan(basePackages = { "com.nvlhnn.order.service.dataaccess", "com.nvlhnn.dataaccess"})
@SpringBootApplication(scanBasePackages = "com.nvlhnn")
public class OrderServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }
}