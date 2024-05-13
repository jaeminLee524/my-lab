package com.study.stockconcurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.study.stockconcurrency.entity.PessimisticStock;
import com.study.stockconcurrency.repository.PessimisticStockRepository;
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
class PessimisticLockServiceTest {

    @Autowired
    private PessimisticLockService pessimisticLockService;

    @Autowired
    private PessimisticStockRepository pessimisticStockRepository;

    @BeforeEach
    void setUp() {
        PessimisticStock stock = new PessimisticStock(1L, 100L);
        pessimisticStockRepository.saveAndFlush(stock);
    }

    @AfterEach
    void tearDown() {
        pessimisticStockRepository.deleteAllInBatch();
    }

    @DisplayName("비관적 락(Pessimistic lock)을 사용해서 동시성을 제어한다.")
    @Test
    void concurrency_with_pessimistic_lock() throws InterruptedException {
        // given
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(30);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                    pessimisticLockService.decreaseStock(1L, 1L);
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();

        // when
        PessimisticStock stock = pessimisticStockRepository.findById(1L).orElseThrow();

        // then
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}