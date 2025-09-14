package com.globallogic.users_service.service;

import com.globallogic.users_service.dto.PhoneRequest;
import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;
import com.globallogic.users_service.exception.UserAlreadyExistsException;
import com.globallogic.users_service.exception.UserNotFoundException;
import com.globallogic.users_service.mapper.UserMapper;
import com.globallogic.users_service.model.User;
import com.globallogic.users_service.repository.UserRepository;
import com.globallogic.users_service.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Instant;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSignUpSuccessful() {
        List<PhoneRequest> phones = new ArrayList<PhoneRequest>();
        PhoneRequest phone = new PhoneRequest(123233L, 1, "57");
        phones.add(phone);

        SignUpRequest request = new SignUpRequest("Juan", "juan@test.com", "password", phones);
        User mockUser = UserMapper.toEntity(request);
        mockUser.setId(UUID.randomUUID());
        mockUser.setPassword("encodedPassword");
        mockUser.setToken("jwtToken");
        mockUser.setActive(true);
        mockUser.setCreated(Instant.now());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(request.getPassword())).thenReturn(mockUser.getPassword());
        when(jwtUtil.generateToken(request.getEmail())).thenReturn(mockUser.getToken());
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        UserResponse response = userService.signUp(request);

        assertNotNull(response);
        assertNotNull(response.getCreated());
        assertTrue(response.isActive());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testSignUpFailsWhenUserAlreadyExists() {

        SignUpRequest request = new SignUpRequest("Juan", "juan@test.com", "password", Collections.emptyList());
        User existingUser = new User();
        existingUser.setEmail(request.getEmail());

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(existingUser));

        assertThrows(UserAlreadyExistsException.class, () -> userService.signUp(request));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginSuccessful() {
        String token = "validJwtToken";
        String email = "juan@test.com";
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail(email);
        user.setLastLogin(Instant.now().minusSeconds(3600));
        user.setToken(token);
        user.setActive(true);

        when(jwtUtil.getSubject(token)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(email)).thenReturn("newJwtToken");

        UserResponse response = userService.login(token);

        assertNotNull(response);
        assertNotNull(response.getLastLogin());
        assertTrue(response.isActive());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testLoginFailsWhenUserNotFound() {
        String token = "invalidJwtToken";

        when(jwtUtil.getSubject(token)).thenReturn("unknown@test.com");
        when(userRepository.findByEmail("unknown@test.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.login(token));
        verify(userRepository, never()).save(any(User.class));
    }
}