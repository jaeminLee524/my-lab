package com.study.bucket4jspringboot.aop;

import com.study.service.RateLimiterService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@RequiredArgsConstructor
@Component
public class RateLimitAspect {

    private final RateLimiterService rateLimiterService;

    @Around("@annotation(com.study.bucket4jspringboot.annotation.RateLimit)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
