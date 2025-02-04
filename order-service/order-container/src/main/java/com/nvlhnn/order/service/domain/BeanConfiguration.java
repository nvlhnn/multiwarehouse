package com.nvlhnn.order.service.domain;

import com.nvlhnn.order.service.domain.properties.XenditProperties;
import com.xendit.Xendit;
import com.xendit.XenditClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class BeanConfiguration {

    private final XenditProperties xenditProperties;

    public BeanConfiguration(XenditProperties xenditProperties) {
        this.xenditProperties = xenditProperties;
    }

    @Bean
    public OrderDomainService orderDomainService() {
        return new OrderDomainServiceImpl();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("*") // Allow all origins
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allow specific HTTP methods
                        .allowedHeaders("*"); // Allow all headers
            }
        };
    }

    @Bean
    public void xendit() {
        Xendit.apiKey = xenditProperties.getSecret();
//        return new XenditClient.Builder()
//                .setApikey(xenditProperties.getSecret())
//                .build();
    }
}
