package com.study.stockconcurrency.service;

import com.study.stockconcurrency.entity.OptimisticStock;
import com.study.stockconcurrency.entity.PessimisticStock;
import com.study.stockconcurrency.repository.OptimisticStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class OptimisticLockService {


    private final OptimisticStockRepository optimisticStockRepository;

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        OptimisticStock stock = optimisticStockRepository.findByIdWithOptimisticLock(id);

        stock.decreaseStock(quantity);

        optimisticStockRepository.saveAndFlush(stock);
    }
}
