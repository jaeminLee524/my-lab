package com.study.service;

import com.study.bucket4jspringboot.config.RateLimiterConfig;
import com.study.bucket4jspringboot.exception.RateLimiterException;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
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

    public boolean tryConsume(String remoteAddrKey) {
        Bucket bucket = getOrCreateBucket(remoteAddrKey);

        ConsumptionProbe probe = consumeToken(bucket);

        logConsumption(remoteAddrKey, probe);

        handleNotConsumed(probe);

        return probe.isConsumed();
    }

    private Bucket getOrCreateBucket(String apiKey) {
        return rateLimiterConfig.lettuceBasedProxyManager().builder()
            .build(apiKey, () -> rateLimiterConfig.bucketConfiguration());
    }

    private ConsumptionProbe consumeToken(Bucket bucket) {
        return bucket.tryConsumeAndReturnRemaining(1);
    }

    private void logConsumption(String remoteAddrKey, ConsumptionProbe probe) {
        log.info("API Key: {}, RemoteAddress: {}, tryConsume: {}, remainToken: {}, tryTime: {}",
            remoteAddrKey, remoteAddrKey, probe.isConsumed(), probe.getRemainingTokens(), LocalDateTime.now());
    }

    private void handleNotConsumed(ConsumptionProbe probe) {
        if (!probe.isConsumed()) {
            throw new RateLimiterException(RateLimiterException.TOO_MANY_REQUEST);
        }
    }

    public long getRemainToken(String apiKey) {
        Bucket bucket = getOrCreateBucket(apiKey);
        return bucket.getAvailableTokens();
    }
}
