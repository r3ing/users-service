package com.globallogic.users_service.mapper;


import com.globallogic.users_service.dto.SignUpRequest;
import com.globallogic.users_service.dto.SignUpResponse;
import com.globallogic.users_service.model.Phone;
import com.globallogic.users_service.model.User;

import java.util.stream.Collectors;

public class UserMapper {

    public static User toEntity(SignUpRequest req) {
        User user = new User();
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

    public static SignUpResponse toSignUpResponse(User user) {
        SignUpResponse resp = new SignUpResponse();
        resp.setId(user.getId().toString());
        resp.setCreated(user.getCreated());
        resp.setLastLogin(user.getLastLogin());
        resp.setToken(user.getToken());
        resp.setActive(user.isActive());

        return resp;
    }

}


