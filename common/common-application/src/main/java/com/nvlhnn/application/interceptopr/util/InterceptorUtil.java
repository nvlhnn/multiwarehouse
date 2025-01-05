package com.nvlhnn.application.interceptopr.util;

import com.nvlhnn.application.interceptopr.HttpMethodAndPathInterceptor;
import com.nvlhnn.application.interceptopr.dto.RouteConfig;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

import java.util.List;

public class InterceptorUtil {

    private InterceptorUtil() {
        // Prevent instantiation
    }

    public static void registerProtectedRoutes(InterceptorRegistry registry,
                                               List<HandlerInterceptor> interceptors,
                                               List<RouteConfig> routes) {
        for (RouteConfig route : routes) {
            for (HandlerInterceptor interceptor : interceptors) {
                registry.addInterceptor(new HttpMethodAndPathInterceptor(interceptor, route.getMethod(), route.getPath()))
                        .addPathPatterns(route.getPath());
            }
        }
    }
}