package com.nvlhnn.product.service.domain.rest;

import com.nvlhnn.application.interceptopr.JwtInterceptor;
import com.nvlhnn.application.interceptopr.RoleInterceptor;
import com.nvlhnn.application.interceptopr.dto.RouteConfig;
import com.nvlhnn.application.interceptopr.util.InterceptorUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

        InterceptorUtil.registerProtectedRoutes(
                registry,
                List.of(jwtInterceptor, roleInterceptor),
                List.of(
                        new RouteConfig("POST", "/products")
                )
        );
    }
}
