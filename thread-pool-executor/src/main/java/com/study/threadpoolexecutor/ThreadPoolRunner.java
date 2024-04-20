package com.study.threadpoolexecutor;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

@Slf4j
@DependsOn("threadPoolTaskExecutor")
@RequiredArgsConstructor
@Component
public class ThreadPoolRunner {

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Async(value = "threadPoolTaskExecutor")
    public void getRemainThreads() {
        int poolSize = threadPoolTaskExecutor.getPoolSize();
        log.info("스레드 풀에 존재하는 현재 스레드 수: {}", poolSize);
    }
}
