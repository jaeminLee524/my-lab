package com.study.stockconcurrency.service;

import com.study.stockconcurrency.entity.PessimisticStock;
import com.study.stockconcurrency.repository.PessimisticStockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PessimisticLockService {


    private final PessimisticStockRepository pessimisticStockRepository;

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        PessimisticStock stock = pessimisticStockRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("재고가 존재하지 않습니다."));

        stock.decreaseStock(quantity);

        pessimisticStockRepository.saveAndFlush(stock);
    }
}
