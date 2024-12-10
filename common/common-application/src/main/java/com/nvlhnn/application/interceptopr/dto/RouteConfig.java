package com.nvlhnn.application.interceptopr.dto;

public class RouteConfig {
    private final String method;
    private final String path;

    public RouteConfig(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }
}
