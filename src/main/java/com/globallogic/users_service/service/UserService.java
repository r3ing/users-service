package com.globallogic.users_service.service;

import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.SignUpResponse;
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

    public SignUpResponse signUp(SignUpRequest signUpRequest) {

        User newUser = UserMapper.toEntity(signUpRequest);
        newUser.setPassword("password");
        newUser.setToken("token");

        User savedUser = userRepository.save(newUser);

        return UserMapper.toSignUpResponse(savedUser);
    }
}
