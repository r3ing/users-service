package com.globallogic.users_service.mapper;


import com.globallogic.users_service.dto.PhoneRequest;
import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.UserResponse;
import com.globallogic.users_service.model.Phone;
import com.globallogic.users_service.model.User;

import java.util.stream.Collectors;

/**
 * Utility class for mapping between User, SignUpRequest, and UserResponse objects.
 * <p>
 * This class provides static methods to:
 * - Convert a SignUpRequest object to a User entity.
 * - Map a User entity to a UserResponse.
 */
public class UserMapper {

    public static User toEntity(SignUpRequest req) {
        var user = new User();
        if (req.getName() != null && !req.getName().isBlank()) {
            user.setName(req.getName());
        }

        user.setEmail(req.getEmail());

        if (req.getPhones() != null) {
            user.setPhones(req.getPhones().stream().map(pr -> {
                Phone p = new Phone();
                p.setNumber(pr.getNumber());
                p.setCityCode(pr.getCitycode());
                p.setCountryCode(pr.getContrycode());
                p.setUser(user);
                return p;
            }).collect(Collectors.toList()));
        }
        return user;
    }

    public static UserResponse toSignUpResponse(User user) {
        var resp = new UserResponse();
        resp.setId(user.getId().toString());
        resp.setCreated(user.getCreated());
        resp.setLastLogin(user.getLastLogin());
        resp.setToken(user.getToken());
        resp.setActive(user.isActive());
        resp.setEmail(user.getEmail());

        if (user.getName() != null) {
            resp.setName(user.getName());
        }

        if (user.getPhones() != null) {
            resp.setPhones(user.getPhones().stream().map(ph -> {
                PhoneRequest pr = new PhoneRequest();
                pr.setNumber(ph.getNumber());
                pr.setCitycode(ph.getCityCode());
                pr.setContrycode(ph.getCountryCode());
                return pr;
            }).collect(Collectors.toList()));
        }
        return resp;
    }
}



