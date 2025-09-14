package com.globallogic.users_service.controller;

import com.globallogic.users_service.model.User;
import com.globallogic.users_service.repository.UserRepository;
import com.globallogic.users_service.security.JwtUtil;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerLoginTest {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void loginShouldReturnUserResponseWhenValidToken() throws Exception {
        String userName = "test@example.com";
        String token = jwtUtil.generateToken(userName);

        User user = new User();
        user.setEmail(userName);
        user.setPassword("password");
        user.setToken(token);
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/users/login")
                        .header("Authorization", "Bearer " + token)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(userName))
                .andExpect(jsonPath("$.token").value(Matchers.not(token)));
    }

    @Test
    void loginWithDifferentTokenInDbShouldReturn401() throws Exception {
        String userName = "test@example.com";
        String tokenSend = jwtUtil.generateToken(userName);
        String currentJWT = jwtUtil.generateToken(userName);

        User user = new User();
        user.setEmail(userName);
        user.setPassword("password");
        user.setToken(currentJWT);
        userRepository.save(user);

        mockMvc.perform(get("/api/v1/users/login")
                        .header("Authorization", "Bearer " + tokenSend))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error[0].codigo").value(401))
                .andExpect(jsonPath("$.error[0].detail").value("Invalid or expired token."));
    }

    @Test
    void loginWithInvalidUserInDbShouldReturn401() throws Exception {
        String userName = "test@example.com";
        String tokenSend = jwtUtil.generateToken(userName);

        mockMvc.perform(get("/api/v1/users/login")
                        .header("Authorization", "Bearer " + tokenSend))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error[0].codigo").value(401))
                .andExpect(jsonPath("$.error[0].detail").value("User not found or invalid token."));
    }


    @Test
    void loginWithMissingAuthorizationHeaderShouldReturn401() throws Exception {

        mockMvc.perform(get("/api/v1/users/login"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error[0].codigo").value(401))
                .andExpect(jsonPath("$.error[0].detail").value("Authorization header missing"));
    }

    @Test
    void loginWithInvalidTokenShouldReturn401() throws Exception {

        mockMvc.perform(get("/api/v1/users/login")
                        .header("Authorization", "Bearer " + "invalid.jwt.token"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error[0].codigo").value(401));
    }
}

