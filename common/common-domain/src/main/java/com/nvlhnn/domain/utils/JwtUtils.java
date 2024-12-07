package com.nvlhnn.domain.utils;

import io.jsonwebtoken.*;
import java.util.Date;
import java.util.Map;

public class JwtUtils {


    public static boolean verifyToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey("secretKey")
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


    public static Map<String, Object> decodeToken(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (ExpiredJwtException e) {
            throw new JwtException("Token has expired", e);
        } catch (JwtException e) {
            throw new JwtException("Token is invalid", e);
        }
    }


    public static Object getClaim(String token, String secretKey, String claimKey) {
        Map<String, Object> claims = decodeToken(token, secretKey);
        if (claims.containsKey(claimKey)) {
            return claims.get(claimKey);
        } else {
            throw new JwtException("Claim not found: " + claimKey);
        }
    }

    public static boolean isTokenExpired(String token, String secretKey) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
            Date expiration = claims.getExpiration();
            return expiration.before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
    }
}
