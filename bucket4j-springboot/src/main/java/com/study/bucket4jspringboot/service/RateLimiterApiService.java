package com.study.bucket4jspringboot.service;

import com.study.bucket4jspringboot.annotation.RateLimit;
import org.springframework.stereotype.Service;

@Service
public class RateLimiterApiService {

    @RateLimit
    public String run() {
        return "성공";
    }
}
