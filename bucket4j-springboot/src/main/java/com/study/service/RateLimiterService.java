package com.study.service;

import com.study.bucket4jspringboot.config.RateLimiterConfig;
import io.github.bucket4j.Bucket;
import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RateLimiterService {

    private final RateLimiterConfig rateLimiterConfig;
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public boolean tryConsume(String remoteAddrKey) {
        Bucket bucket = getOrCreateBucket(remoteAddrKey);

        boolean tryConsume = bucket.tryConsume(1);

        log.info("API Key: {}, RemoteAddress: {}, tryConsume: {}, remainToken: {}, tryTime: {}",
            remoteAddrKey, remoteAddrKey, tryConsume,
            getRemainToken(remoteAddrKey), LocalDateTime.now());

        return tryConsume;
    }

    private Bucket getOrCreateBucket(String apiKey) {
        return buckets.computeIfAbsent(apiKey, this::newBucket);
    }

    private Bucket newBucket(String apiKey) {
        return rateLimiterConfig.initBucket(apiKey);
    }

    public long getRemainToken(String apiKey) {
        Bucket bucket = getOrCreateBucket(apiKey);
        return bucket.getAvailableTokens();
    }
}
