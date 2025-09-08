package com.globallogic.users_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationMillis}")
    private long expirationMillis;

    public String generateToken(String email) {

        return JWT.create()
                .withSubject(email)
                .withIssuedAt(new Date())
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(Algorithm.HMAC256(secret));
    }


    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public boolean validateToken(String token) {
        return JWT.require(Algorithm.HMAC256(secret)).build().verify(token).getSubject() != null;
    }


}
