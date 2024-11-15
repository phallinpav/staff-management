package com.sample.account_service.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CustomErrorResponse {

    private final LocalDateTime timestamp;
    private final int status;
    String error;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<String> messages;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    String path;

    public CustomErrorResponse(HttpStatus httpStatus) {
        this.timestamp = LocalDateTime.now();
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }

    public CustomErrorResponse(HttpStatus httpStatus, String message) {
        this(httpStatus);
        this.message = message;
    }

    public CustomErrorResponse(HttpStatus httpStatus, List<String> messages) {
        this(httpStatus);
        this.messages = messages;
    }

}
