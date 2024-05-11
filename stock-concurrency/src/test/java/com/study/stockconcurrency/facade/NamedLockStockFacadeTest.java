package com.study.stockconcurrency.facade;

import static org.assertj.core.api.Assertions.assertThat;

import com.study.stockconcurrency.entity.NamedStock;
import com.study.stockconcurrency.repository.NamedLockStockRepository;
import com.study.stockconcurrency.service.NamedLockStockService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class NamedLockStockFacadeTest {

    @Autowired
    private NamedLockStockService namedLockStockService;

    @Autowired
    private NamedLockStockRepository namedLockStockRepository;

    @BeforeEach
    void setUp() {
        NamedStock namedStock = new NamedStock(1L, 100L);
        namedLockStockRepository.saveAndFlush(namedStock);
    }

    @AfterEach
    void tearDown() {
        namedLockStockRepository.deleteAllInBatch();
    }

    @DisplayName("네임드 락(Named lock)을 사용해서 동시성을 제어한다.")
    @Test
    void concurrency_with_named_lock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    namedLockStockService.decreaseStock(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        NamedStock stock = namedLockStockRepository.findById(1L).orElseThrow();
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}