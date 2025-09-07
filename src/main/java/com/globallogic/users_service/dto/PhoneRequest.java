package com.globallogic.users_service.dto;

import lombok.Data;

@Data
public class PhoneRequest {
    private Long number;
    private Integer citycode;
    private String contrycode;
}
