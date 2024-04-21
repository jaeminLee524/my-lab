package com.study.threadpoolexecutor;

import static org.assertj.core.api.Assertions.*;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

@TestInstance(Lifecycle.PER_CLASS)
@TestMethodOrder(OrderAnnotation.class)
public class ThreadPoolExecutorTest {

    Callable<Boolean> sleepTask;
    ThreadPoolExecutor threadPool;
    Future<Boolean> future;

    @BeforeAll
    void setUp() {
        sleepTask = () -> {
            Thread.sleep(1000);
            return true;
        };
    }

    @Test
    @Order(0)
    void createThreadPool() {
        final int corePoolSize = 2;
        final int maxPoolSize = 3;
        final int queueCapacity = 1;
        final long keepAliveTime = 1L;


        threadPool = new ThreadPoolExecutor(corePoolSize,
            maxPoolSize,
            keepAliveTime,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(queueCapacity));
    }

    @Test
    @Order(1)
    void createFirstThread() {
        future = threadPool.submit(sleepTask);

        assertThat(threadPool.getPoolSize()).isOne();
        assertThat(threadPool.getQueue().size()).isZero();
    }

    @Test
    @Order(2)
    void reachCorePoolSize() {
        future = threadPool.submit(sleepTask);

        assertThat(threadPool.getPoolSize()).isEqualTo(2);
        assertThat(threadPool.getQueue().size()).isZero();
    }

    @Test
    @Order(3)
    void reachQueueCapacity() {
        future = threadPool.submit(sleepTask);

        assertThat(threadPool.getPoolSize()).isEqualTo(2);
        assertThat(threadPool.getQueue().size()).isOne();
    }

    @Test
    @Order(4)
    void reachMaxPoolSize() {
        future = threadPool.submit(sleepTask);

        assertThat(threadPool.getPoolSize()).isEqualTo(3);
        assertThat(threadPool.getQueue().size()).isEqualTo(1);
    }

}
