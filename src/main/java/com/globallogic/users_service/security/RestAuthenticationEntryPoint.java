package com.globallogic.users_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.users_service.dto.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * The RestAuthenticationEntryPoint class is a custom implementation of
 * the {@link AuthenticationEntryPoint} interface, used in Spring Security
 * to handle unauthorized access attempts.
 * This class is responsible for intercepting unauthorized requests
 * and providing a standardized JSON response with a 401 (Unauthorized)
 * status code. It also generates an error response containing a detailed
 * message based on the presence and validity of the Authorization header.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper mapper;

    public RestAuthenticationEntryPoint(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        String authHeader = request.getHeader("Authorization");

        String message = (authHeader == null) ? "Authorization header missing" :
                (!authHeader.startsWith("Bearer ")) ? "Authorization header must start with Bearer" :
                        "Invalid Token";

        ErrorResponse body = new ErrorResponse(Instant.now(), HttpServletResponse.SC_UNAUTHORIZED, message);
        response.getWriter().write(mapper.writeValueAsString(body));

    }
}
