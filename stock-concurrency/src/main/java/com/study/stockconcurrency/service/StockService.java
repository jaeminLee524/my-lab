package com.study.stockconcurrency.service;

import com.study.stockconcurrency.entity.Stock;
import com.study.stockconcurrency.repository.StockRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class StockService {

    private final StockRepository stockRepository;

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        Stock stock = stockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고가 존재하지 않습니다."));

        stock.decreaseStock(quantity);

        stockRepository.saveAndFlush(stock);
    }
}
