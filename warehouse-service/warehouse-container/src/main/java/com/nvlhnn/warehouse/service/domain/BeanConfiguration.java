package com.nvlhnn.warehouse.service.domain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class BeanConfiguration {

    @Bean
    public WarehouseDomainService warehouseDomainServiceDomainService() {
        return new WarehouseDomainServiceImpl();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {  // Use WebMvcConfigurer instead of WebMvcConfigurerAdapter
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // Specify your frontend's origin
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
                        .allowedHeaders("Authorization", "Content-Type") // Allowed headers
                        .allowCredentials(true) // Allow credentials (cookies, authorization headers)
                        .maxAge(3600); // Cache preflight response for 1 hour
            }
        };
    }
}
