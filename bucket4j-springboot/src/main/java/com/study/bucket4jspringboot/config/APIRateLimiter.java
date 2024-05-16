package com.study.bucket4jspringboot.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.BucketConfiguration;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class APIRateLimiter {

    // 버킷에 담길 수 있는 토큰의 최대 수
    private static final int CAPACITY = 3;

    // REFILL_DURATION 으로 지정된 시간 동안 버킷에 추가될 토큰의 수
    private static final int REFILL_TOKEN_AMOUNT = 3;

    // 토큰이 재충전되는 빈도
    private static final Duration REFILL_DURATION = Duration.ofSeconds(5);

    private final BucketManager bucketManager;

    public APIRateLimiter(BucketManager bucketManager) {
        this.bucketManager = bucketManager;
    }

    public boolean tryConsume(String apiKey) {
        BucketConfiguration bucketConfiguration = createBucketConfiguration();
        boolean tryConsume = bucketManager.getOrCreateBucket(apiKey, bucketConfiguration).tryConsume(1);

        log.info("API Key: {}, tryConsume: {}, tryTime: {}", apiKey, tryConsume, LocalDateTime.now());

        return tryConsume;
    }

    private BucketConfiguration createBucketConfiguration() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.simple(CAPACITY, REFILL_DURATION).withInitialTokens(REFILL_TOKEN_AMOUNT))
            .build();
    }
}
