package com.globallogic.users_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.globallogic.users_service.dto.PhoneRequest;
import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;
import com.globallogic.users_service.exception.UserAlreadyExistsException;
import com.globallogic.users_service.security.JwtRequestFilter;
import com.globallogic.users_service.service.UserServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtRequestFilter jwtRequestFilter;

    @MockBean
    private UserServiceImpl userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldSuccessfullySignUpUserWithValidInput() throws Exception {

        SignUpRequest signUpRequest = new SignUpRequest("Julio", "julio@example.com", "Password12",
                List.of(new PhoneRequest(87650009L, 1, "57")));

        UserResponse userResponse = new UserResponse(
                UUID.randomUUID().toString(),
                Instant.now(),
                Instant.now(),
                "jwt_token",
                true,
                "Julio",
                "julio@example.com",
                List.of(new PhoneRequest(87650009L, 1, "57"))
        );

        when(userService.signUp(signUpRequest)).thenReturn(userResponse);

        mockMvc.perform(post("/api/v1/users/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signUpRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(userResponse)))
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void shouldReturnBadRequestForMissingEmail() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Julio", null, "Password12", Collections.emptyList());

        mockMvc.perform(
                        post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error[0].detail").value("The email cannot be empty."));

    }

    @Test
    void shouldReturnBadRequestForInvalidEmail() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Julio", "invalid-email", "Password12", Collections.emptyList());

        mockMvc.perform(
                        post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].codigo").value(HttpStatus.BAD_REQUEST.value()))
                .andExpect(jsonPath("$.error[0].detail").value("Email format is invalid."));
    }

    @Test
    void shouldReturnBadRequestForMissingPassword() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Julio", "julio@email.com", null, Collections.emptyList());


        mockMvc.perform(
                        post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("The password cannot be empty."));
    }

    @Test
    void shouldReturnBadRequestForInvalidPassword() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Julio", "julio@email.com", "invalidPassword", Collections.emptyList());


        mockMvc.perform(
                        post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error[0].detail").value("Password must be 8-12 chars, contain exactly one uppercase letter and exactly two digits; only letters and digits are allowed."));
    }

    @Test
    void signUp_EmailAlreadyRegistered() throws Exception {
        SignUpRequest signUpRequest = new SignUpRequest("Julio", "julio@email.com", "Password12", Collections.emptyList());

        when(userService.signUp(Mockito.any(SignUpRequest.class)))
                .thenThrow(new UserAlreadyExistsException("User with email already exists: " + signUpRequest.getEmail()));

        mockMvc.perform(
                        post("/api/v1/users/sign-up")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(signUpRequest))
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error[0].detail").value("User with email already exists: " + signUpRequest.getEmail()));
    }

    @Test
    void loginShouldReturnUserResponse() throws Exception {

        String fakeToken = "fake_jwt_token";

        UserResponse userResponse = new UserResponse(
                UUID.randomUUID().toString(),
                Instant.now(),
                Instant.now(),
                "new_jwt_token",
                true,
                "Julio",
                "julio@example.com",
                List.of(new PhoneRequest(87650009L, 1, "57"))
        );

        when(userService.login(fakeToken)).thenReturn(userResponse);

        mockMvc.perform(get("/api/v1/users/login")
                        .header("Authorization", "Bearer " + fakeToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(Matchers.not(fakeToken)));

        verify(userService).login(fakeToken);
    }
}