package com.siddh.api_gateway.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.List;


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
    public Long extractUserId(String token){
        Claims claims=Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getBody();

        Number userId=claims.get("userId", Number.class);
        return userId!=null ? userId.longValue() : null;
    }

    public List<String> extractRoles(String token) {
        //taking data
        Claims claims = Jwts.parser()
                .verifyWith(secretKey) // or getSignKey() depending on your setup
                .build()
                .parseSignedClaims(token)
                .getBody();


        //System.out.println("CLAIMS: " + claims);

        //get the role
        Object rolesObject = claims.get("roles");
        //System.out.println("ROLES : " + rolesObject);

        return (List<String>) rolesObject;
    }
}
