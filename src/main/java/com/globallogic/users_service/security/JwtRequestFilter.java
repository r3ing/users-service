package com.globallogic.users_service.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.users_service.dto.ErrorResponse;
import com.globallogic.users_service.model.User;
import com.globallogic.users_service.repository.UserRepository;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

/**
 * The JwtRequestFilter class is responsible for processing incoming HTTP requests
 * and validating JWT tokens. This filter ensures that only valid JWT tokens are allowed and sets the security
 * context for later usage in the application.
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    public JwtRequestFilter(JwtUtil jwtUtil, UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String jwt = authHeader.substring(7).trim();

            jwtUtil.validateToken(jwt);

            String username = jwtUtil.getSubject(jwt);

            Optional<User> dbUser = userRepository.findByEmail(username);

            if (dbUser.isEmpty()) {
                sendUnauthorized(response, "User not found or invalid token.");
                return;
            }

            if (!jwt.equals(dbUser.get().getToken())) {
                sendUnauthorized(response, "Invalid or expired token.");
                return;
            }

            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(username, null, Collections.emptyList());

            SecurityContextHolder.getContext().setAuthentication(authToken);


        } catch (JWTVerificationException ex) {
            sendUnauthorized(response, ex.getMessage());
            return;
        }


        filterChain.doFilter(request, response);
    }

    private void sendUnauthorized(HttpServletResponse response, String detail) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        ErrorResponse body = new ErrorResponse(Instant.now(), HttpServletResponse.SC_UNAUTHORIZED, detail);
        objectMapper.writeValue(response.getWriter(), body);
    }
}
