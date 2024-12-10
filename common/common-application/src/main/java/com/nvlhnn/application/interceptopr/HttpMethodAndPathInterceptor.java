package com.nvlhnn.application.interceptopr;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class HttpMethodAndPathInterceptor implements HandlerInterceptor {

    private final HandlerInterceptor delegate;
    private final String method;
    private final String pathPattern;

    public HttpMethodAndPathInterceptor(HandlerInterceptor delegate, String method, String pathPattern) {
        this.delegate = delegate;
        this.method = method;
        this.pathPattern = pathPattern;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestPath = request.getRequestURI();
        if (method.equalsIgnoreCase(request.getMethod()) && requestPath.matches(convertToRegex(pathPattern))) {
            return delegate.preHandle(request, response, handler);
        }
        return true; // Skip for non-matching methods or paths
    }

    private String convertToRegex(String pathPattern) {
        return pathPattern.replace("*", ".*");
    }
}