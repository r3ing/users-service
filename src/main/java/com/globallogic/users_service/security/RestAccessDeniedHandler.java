package com.globallogic.users_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.users_service.dto.ErrorResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Instant;

/**
 * RestAccessDeniedHandler is responsible for handling situations where a user
 * attempts to access a resource that they do not have sufficient permissions for.
 * This class implements the {@link AccessDeniedHandler} interface and is
 * typically used in Spring Security configurations to provide a standardized
 * JSON response with a 403 (Forbidden) status in case of access denial.
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper mapper;

    public RestAccessDeniedHandler(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse body = new ErrorResponse(Instant.now(), HttpServletResponse.SC_FORBIDDEN, "Access denied");
        response.getWriter().write(mapper.writeValueAsString(body));

    }
}
