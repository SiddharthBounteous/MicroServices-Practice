package com.siddh.api_gateway.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;


@Component
public class JWTUtil {
    private final SecretKey secretKey;

    public JWTUtil(@Value("${app.jwt.secret}") String secretKey){
        this.secretKey= Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public void validateToken(String token){
        Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token);
    }
}
