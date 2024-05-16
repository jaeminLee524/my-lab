package com.study.bucket4jspringboot.exception;

import static org.springframework.http.HttpStatus.TOO_MANY_REQUESTS;

import com.study.bucket4jspringboot.exception.RateLimiterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RateLimiterException.class)
    public ResponseEntity<String> RateLimiterException(RateLimiterException exception) {
        return new ResponseEntity<>(exception.getMessage(), TOO_MANY_REQUESTS);
    }
}
