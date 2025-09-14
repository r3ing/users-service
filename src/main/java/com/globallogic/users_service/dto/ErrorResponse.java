package com.globallogic.users_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.Collections;
import java.util.List;

/**
 * Represents an error response containing one or more error details.
 * This is used for providing information about errors in a structured format.
 */
@Setter
@Getter
@NoArgsConstructor
public class ErrorResponse {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorItem {
        private Instant timestamp;
        private int codigo;
        private String detail;

    }

    private List<ErrorItem> error;

    public ErrorResponse(Instant timestamp, int codigo, String detail) {
        this.error = Collections.singletonList(new ErrorItem(timestamp, codigo, detail));
    }

}
