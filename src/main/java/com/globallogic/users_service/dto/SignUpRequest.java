package com.globallogic.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SignUpRequest {
    private String name;
    private String email;
    private String password;
    private List<PhoneRequest> phones;

}
