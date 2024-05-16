package com.study.bucket4jspringboot.config;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.distributed.ExpirationAfterWriteStrategy;
import io.github.bucket4j.redis.lettuce.cas.LettuceBasedProxyManager;
import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.codec.ByteArrayCodec;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.codec.StringCodec;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BucketManager {

    private final LettuceBasedProxyManager<String> proxyManager;
    private final ConcurrentMap<String, Bucket> buckets = new ConcurrentHashMap<>();

    public BucketManager(RedisClient redisClient) {
        StatefulRedisConnection<String, byte[]> connection = redisClient.connect(RedisCodec.of(StringCodec.UTF8, ByteArrayCodec.INSTANCE));

        // Redis 연결을 이용해 LettuceBasedProxyManager 객체를 생성
        this.proxyManager = LettuceBasedProxyManager.builderFor(connection)
            // 버킷 만료 정책(최대 용량으로 채워지는데 필요한 60초 동안 버킷으로 유지)
            .withExpirationStrategy(ExpirationAfterWriteStrategy.basedOnTimeForRefillingBucketUpToMax(Duration.ofSeconds(60)))
            .build();
    }

    public Bucket getOrCreateBucket(String apiKey, BucketConfiguration configuration) {
        return buckets.computeIfAbsent(apiKey, key -> proxyManager.builder().build(key, configuration));
    }
}
