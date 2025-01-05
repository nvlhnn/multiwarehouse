package com.nvlhnn.order.service.domain;

import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Bean;

/**
 * Test configuration for the Order Service domain tests.
 * It mocks all the necessary dependencies using Mockito, except for OrderDomainService.
 */
@SpringBootConfiguration
@TestConfiguration
@ComponentScan(basePackages = "com.nvlhnn.order.service.domain")
public class OrderTestConfiguration {

    // Define PasswordEncoder bean if needed by OrderDomainService

    // If you prefer to manually define OrderDomainService, add it here
    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }

    // Note: It's preferable to let Spring auto-detect OrderDomainServiceImpl via @Service
}
