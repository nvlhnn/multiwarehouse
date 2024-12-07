package com.nvlhnn.application.interceptopr;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private final String SECRET_KEY = "secretKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String jwtToken = extractJwtToken(request);

        log.debug("JWT Token: {}", jwtToken);

        if (jwtToken != null && validateJwtToken(jwtToken)) {
            Claims claims = parseClaims(jwtToken);

            String userId = claims.get("userId", String.class);
            String role = claims.get("role", String.class); // Extract role from JWT claims

            // Set userId and role as request attributes for later interceptors
            request.setAttribute("userId", userId);
            request.setAttribute("role", role);

            log.info("User ID: {}", userId);
            log.info("Role: {}", role);
        } else {
            log.warn("JWT Token validation failed.");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid or missing JWT token.");
            return false;  // Reject the request if the JWT token is invalid
        }

        return true;  // Allow the request to continue if the JWT is valid
    }

    private String extractJwtToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);  // Extract the token part after "Bearer "
        }

        return null; // Return null if token is not found
    }

    private boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            log.error("Invalid JWT token", e);
            return false;  // Return false if token validation fails
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
