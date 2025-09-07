package com.globallogic.users_service.service;

import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;
import com.globallogic.users_service.exception.UserAlreadyExistsException;
import com.globallogic.users_service.mapper.UserMapper;
import com.globallogic.users_service.model.User;
import com.globallogic.users_service.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse signUp(SignUpRequest signUpRequest) {

        userRepository.findByEmail(signUpRequest.getEmail()).ifPresent(user -> {
            throw new UserAlreadyExistsException("User with email already exists: " + signUpRequest.getEmail());
        });

        User newUser = UserMapper.toEntity(signUpRequest);
        newUser.setPassword("password");
        newUser.setToken("token");

        User savedUser = userRepository.save(newUser);

        return UserMapper.toSignUpResponse(savedUser);
    }
}
