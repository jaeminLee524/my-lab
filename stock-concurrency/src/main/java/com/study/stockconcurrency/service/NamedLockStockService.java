package com.study.stockconcurrency.service;


import com.study.stockconcurrency.entity.NamedStock;
import com.study.stockconcurrency.repository.NamedLockStockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class NamedLockStockService {

    private final NamedLockStockRepository namedLockStockRepository;

    @Transactional
    public void decreaseStock(Long id, Long quantity) {
        namedLockStockRepository.getLock(String.valueOf(id));

        try {
            NamedStock namedStock = namedLockStockRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("재고가 존재하지 않습니다."));

            namedStock.decreaseStock(quantity);

            namedLockStockRepository.saveAndFlush(namedStock);

            log.info("stock: {}", namedStock);
        } catch (IllegalArgumentException e) {
            System.out.println("재고 감소 중 예외 발생: " + e.getMessage());
        } finally {
            namedLockStockRepository.releaseLock(String.valueOf(id));
        }
    }
}
