package com.globallogic.users_service.service;

import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;

public interface UserService {

    UserResponse signUp(SignUpRequest signUpRequest);

    UserResponse login(String token);
}
