package com.globallogic.users_service.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

/**
 * A data transfer object that represents the response provided to the user
 * after a successful sign-up or login operation.
 * <p>
 * This class is primarily used to transfer user-related information, such as
 * account creation details, authentication token, and personal details.
 */
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
