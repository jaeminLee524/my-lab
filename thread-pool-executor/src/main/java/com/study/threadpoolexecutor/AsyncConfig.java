package com.study.threadpoolexecutor;

import java.util.concurrent.Executor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "threadPoolTaskExecutor")
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5); // core pool size를 지정합니다.
//        executor.setPrestartAllCoreThreads(true); // core pool size만큼의 스레드를 미리 생성합니다.
        executor.setMaxPoolSize(20); // 최대 pool size를 지정합니다.
        executor.setQueueCapacity(11); // queue에 들어갈 수 있는 요청 수를 지정합니다.
        executor.setThreadNamePrefix("async-service-");
        executor.setRejectedExecutionHandler(new CustomRejectedExecutionHandler());
        executor.initialize();

        return executor;
    }
}
