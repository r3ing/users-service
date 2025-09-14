package com.globallogic.users_service.service;

import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;

/**
 * The UserService interface provides a contract for user-related operations,
 * specifically user sign-up and login functionalities.
 */
public interface UserService {

    UserResponse signUp(SignUpRequest signUpRequest);

    UserResponse login(String token);
}
