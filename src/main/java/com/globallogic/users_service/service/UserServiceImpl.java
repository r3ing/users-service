package com.globallogic.users_service.service;

import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;
import com.globallogic.users_service.exception.UserAlreadyExistsException;
import com.globallogic.users_service.exception.UserNotFoundException;
import com.globallogic.users_service.mapper.UserMapper;
import com.globallogic.users_service.model.User;
import com.globallogic.users_service.repository.UserRepository;
import com.globallogic.users_service.security.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @Transactional
    @Override
    public UserResponse signUp(SignUpRequest signUpRequest) {

        userRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistsException("User with email already exists: " + signUpRequest.getEmail());
        });

        User newUser = UserMapper.toEntity(signUpRequest);
        newUser.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        newUser.setToken(jwtUtil.generateToken(signUpRequest.getEmail()));

        User savedUser = userRepository.save(newUser);

        return UserMapper.toSignUpResponse(savedUser);
    }

    @Transactional
    @Override
    public UserResponse login(String token) {
        String email = jwtUtil.getSubject(token);

        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found"));

        user.setLastLogin(Instant.now());
        user.setToken(jwtUtil.generateToken(email));

        userRepository.save(user);

        return UserMapper.toSignUpResponse(user);

    }


}
