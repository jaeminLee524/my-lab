package com.study.bucket4jspringboot.exception;

public class RateLimiterException extends RuntimeException {

    public static final String TOO_MANY_REQUEST = "Too many requests. Please try again later.";

    public RateLimiterException(String message) {
        super(message);
    }
}
