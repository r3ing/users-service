package com.globallogic.users_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private String id;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", timezone = "America/Santiago")
    private Instant created;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", timezone = "America/Santiago")
    private Instant lastLogin;
    private String token;

    @JsonProperty("isActive")
    private boolean isActive;
    private String name;
    private String email;
    private List<PhoneRequest> phones;

}
