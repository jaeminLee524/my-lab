package com.study.stockconcurrency.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.study.stockconcurrency.entity.OptimisticStock;
import com.study.stockconcurrency.facade.OptimisticLockStockFacade;
import com.study.stockconcurrency.repository.OptimisticStockRepository;
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
class OptimisticLockServiceTest {

    @Autowired
    private OptimisticLockStockFacade optimisticLockStockFacade;

    @Autowired
    private OptimisticStockRepository optimisticStockRepository;

    @BeforeEach
    void setUp() {
        OptimisticStock stock = new OptimisticStock(1L, 100L);
        optimisticStockRepository.saveAndFlush(stock);
    }

    @AfterEach
    void tearDown() {
        optimisticStockRepository.deleteAllInBatch();
    }

    @Test
    @DisplayName("낙관적 락(Optimistic lock)을 사용해서 동시성을 제어한다.")
    void concurrency_with_optimistic_lock() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch countDownLatch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.submit(() -> {
                try {
                     optimisticLockStockFacade.decreaseStock(1L, 1L);
                } catch (InterruptedException e) {
                    System.out.println("InterruptedException");
                } finally {
                    countDownLatch.countDown();
                }
            });
        }

        countDownLatch.await();
        OptimisticStock stock = optimisticStockRepository.findById(1L).orElseThrow();
        System.out.println("stock = " + stock);
        assertThat(stock.getQuantity()).isEqualTo(0L);
    }
}