package com.globallogic.users_service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.UUID;

/**
 * The JwtUtil class is a utility class for handling JSON Web Tokens (JWT).
 * It provides methods for generating, decoding, and validating JWT tokens.
 * This class is used for implementing secure authentication mechanisms.
 */
@Component
public class JwtUtil {

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expirationMillis}")
    private long expirationMillis;

    public String generateToken(String email) {

        return JWT.create()
                .withJWTId(UUID.randomUUID().toString())
                .withSubject(email)
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationMillis))
                .sign(Algorithm.HMAC256(secret));
    }

    public String getSubject(String token) {
        return JWT.decode(token).getSubject();
    }

    public void validateToken(String token) {
        JWT.require(Algorithm.HMAC256(secret)).build().verify(token);
    }


}
