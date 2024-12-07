package com.nvlhnn.warehouse.service.domain.rest;

import com.nvlhnn.application.interceptopr.JwtInterceptor;
import com.nvlhnn.application.interceptopr.RoleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final JwtInterceptor jwtInterceptor;
    private final RoleInterceptor roleInterceptor;

    public WebConfig(JwtInterceptor jwtInterceptor, RoleInterceptor roleInterceptor) {
        this.jwtInterceptor = jwtInterceptor;
        this.roleInterceptor = roleInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor)
                .addPathPatterns("/warehouses/**");

        // Register RoleInterceptor to check the role of the user
        registry.addInterceptor(roleInterceptor)
                .addPathPatterns("/warehouses/**");
    }
}
