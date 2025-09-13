package com.globallogic.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a request containing phone details.
 * This class is used for transferring phone information as part of
 * requests or mappings between different models or layers.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequest {
    private Long number;
    private Integer citycode;
    private String contrycode;
}
