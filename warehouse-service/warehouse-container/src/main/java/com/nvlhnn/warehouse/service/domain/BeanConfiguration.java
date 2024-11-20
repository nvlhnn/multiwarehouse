package com.nvlhnn.warehouse.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public WarehouseDomainService warehouseDomainServiceDomainService() {
        return new WarehouseDomainServiceImpl();
    }
}
