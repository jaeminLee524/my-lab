package com.study.bucket4jspringboot.aop;

import com.study.bucket4jspringboot.config.APIRateLimiter;
import com.study.bucket4jspringboot.exception.RateLimiterException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@RequiredArgsConstructor
@Component
public class RateLimitAspect {

    private final APIRateLimiter apiRateLimiter;

    @Around("@annotation(com.study.bucket4jspringboot.annotation.RateLimit)")
    public Object checkRateLimit(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (apiRateLimiter.tryConsume(httpServletRequest.getRemoteAddr()) ) {
            return joinPoint.proceed();
        } else {
            throw new RateLimiterException(RateLimiterException.TOO_MANY_REQUEST);
        }
    }
}
