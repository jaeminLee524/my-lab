package com.study.controller;

import com.study.service.RateLimiterApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RestController
public class RateLimiterController {

    private final RateLimiterApiService rateLimiterApiService;

    @GetMapping("")
    public String api() {
        return rateLimiterApiService.run();
    }
}
