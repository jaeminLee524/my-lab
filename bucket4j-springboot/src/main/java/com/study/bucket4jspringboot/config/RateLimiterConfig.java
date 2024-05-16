package com.study.bucket4jspringboot.config;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.Refill;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class RateLimiterConfig {

    // 버킷에 담길 수 있는 토큰의 최대 수
    private static final int CAPACITY = 3;

    // REFILL_DURATION 으로 지정된 시간 동안 버킷에 추가될 토큰의 수
    private static final int REFILL_TOKEN_AMOUNT = 3;

    // 토큰이 재충전되는 빈도
    private static final Duration REFILL_DURATION = Duration.ofSeconds(5);

    private final RedisClient lettuceRedisClient;

    @Bean
    public LettuceBasedProxyManager lettuceBasedProxyManager() {
        return LettuceBasedProxyManager.builderFor(lettuceRedisClient)
            // 버킷 만료 정책(최대 용량으로 채워지는데 필요한 60초 동안 버킷으로 유지)
            .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(60)))
            .build();
    }

    @Bean
    public BucketConfiguration bucketConfiguration() {
        return BucketConfiguration.builder()
            .addLimit(Bandwidth.classic(CAPACITY, Refill.intervally(REFILL_TOKEN_AMOUNT, REFILL_DURATION)))
            .build();
    }

    public Bucket initBucket(String apiKey) {
        return Bucket.builder()
            .addLimit(Bandwidth.classic(CAPACITY, Refill.intervally(REFILL_TOKEN_AMOUNT, REFILL_DURATION)))
            .build();
    }
}
