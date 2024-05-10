package com.study.stockconcurrency.facade;

import com.study.stockconcurrency.service.OptimisticLockService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OptimisticLockStockFacade {

    private final OptimisticLockService optimisticLockService;

    public void decreaseStock(Long id, Long quantity) throws InterruptedException {
        while (true) {
            try {
                optimisticLockService.decreaseStock(id, quantity);
                break;
            } catch (Exception e) {
                Thread.sleep(1);
            }
        }
    }
}
