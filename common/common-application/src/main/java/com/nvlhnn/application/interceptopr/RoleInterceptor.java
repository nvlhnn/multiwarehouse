package com.nvlhnn.application.interceptopr;

import com.nvlhnn.domain.valueobject.UserRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class RoleInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String roleStr = (String) request.getAttribute("role");

        if (roleStr == null) {
            log.warn("Role is missing in the request.");
            throw new AuthenticationException("Role is missing in the request.");
        }

        UserRole role;
        try {
            role = UserRole.valueOf(roleStr);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid role: {}", roleStr);
            throw new AuthenticationException("Invalid role: " + roleStr);
        }

        if (role != UserRole.WAREHOUSE_ADMIN && role != UserRole.SUPER_ADMIN) {
            log.warn("Unauthorized access attempt by user with role: {}", role);
            throw new AuthenticationException("Unauthorized access attempt by user with role: " + role);
        }

        log.info("User role {} is authorized.", role);
        return true;
    }
}
