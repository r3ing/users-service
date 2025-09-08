package com.globallogic.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PhoneRequest {
    private Long number;
    private Integer citycode;
    private String contrycode;
}
