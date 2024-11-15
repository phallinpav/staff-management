package com.sample.account_service.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<CustomErrorResponse> handleException(DataIntegrityViolationException ex) {
        final CustomErrorResponse response;
        if (ex.getCause() instanceof org.hibernate.exception.ConstraintViolationException ex2) {
            if (ex2.getCause() instanceof SQLIntegrityConstraintViolationException ex3) {
                response = new CustomErrorResponse(HttpStatus.CONFLICT, ex3.getMessage());
                return new ResponseEntity<>(response, HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomErrorResponse> handleInternalError(Exception e) {
        log.error("Exception", e);
        final CustomErrorResponse response = new CustomErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.warn("invalid method argument {}", request.getDescription(true));
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, "method");
        return new ResponseEntity<>(error, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String message = "Required request body is missing";
        final String msg = ex.getMessage();
        if (msg != null && !msg.contains(message)) {
            message = msg;
        }
        log.warn("missing body {}", request.getDescription(true));
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.BAD_REQUEST, message);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<String> details = new ArrayList<>();
        for (ObjectError error : ex.getBindingResult().getAllErrors()) {
            if (error instanceof FieldError fe) {
                details.add(String.format("%s : %s", fe.getField(), error.getDefaultMessage()));
            } else {
                details.add(error.getDefaultMessage());
            }
        }
        log.warn("invalid bind {}", request.getDescription(true));
        CustomErrorResponse error = new CustomErrorResponse(HttpStatus.BAD_REQUEST, details);
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

}