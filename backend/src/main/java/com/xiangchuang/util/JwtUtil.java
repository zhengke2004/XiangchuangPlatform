package com.xiangchuang.util;

import com.xiangchuang.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final Long expire;

    public JwtUtil(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.expire = jwtConfig.getExpire();
    }

    public String generateToken(Integer userId, String phone, String username, String userType) {
        return Jwts.builder()
                .claim("userId", userId)
                .claim("phone", phone)
                .claim("username", username)
                .claim("userType", userType)
                .setExpiration(new Date(System.currentTimeMillis() + expire))
                .signWith(secretKey)
                .compact();
    }

    public Claims parseToken(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public Integer getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        return claims.get("userId", Integer.class);
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = parseToken(token);
            return claims.getExpiration().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }

}