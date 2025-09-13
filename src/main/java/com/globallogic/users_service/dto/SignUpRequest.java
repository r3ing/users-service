package com.globallogic.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

/**
 * A data transfer object representing a user's sign-up request. This class is primarily used to capture
 * user data during the registration process.
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class SignUpRequest {
    private String name;

    @NotBlank(message = "The email cannot be empty.")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
            message = "Email format is invalid")
    private String email;

    @NotBlank(message = "The password cannot be empty")
    @Pattern(regexp = "^(?=(?:.*[A-Z]){1})(?!.*[A-Z].*[A-Z])(?=(?:.*\\d){2})(?!.*\\d.*\\d.*\\d)[A-Za-z0-9]{8,12}$",
            message = "Password must be 8-12 chars, contain exactly one uppercase letter and exactly two digits; " +
                    "only letters and digits are allowed.")
    private String password;

    private List<PhoneRequest> phones;

}
