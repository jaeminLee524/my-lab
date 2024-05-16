package com.study.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RateLimiterServiceTest {

    @Autowired
    private RateLimiterService rateLimiterService;

    @Autowired
    private RedisTemplate redisTemplate;

    @AfterEach
    void tearDown() {
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @DisplayName("처리율 제한에 걸리지 않으면 true를 반환한다.")
    @Test
    void return_true_if_not_rate_limit() {
        // given
        String ipKey = "127.0.0.1";

        // when
        boolean result = rateLimiterService.tryConsume(ipKey);

        // then
        Assertions.assertThat(result).isTrue();
    }
}