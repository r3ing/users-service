package com.globallogic.users_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {

    private String id;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", timezone = "America/Santiago")
    private Instant created;
    @JsonFormat(pattern = "MMM dd, yyyy h:mm:ss a", timezone = "America/Santiago")
    private Instant lastLogin;
    private String token;
    private Boolean active;
}
