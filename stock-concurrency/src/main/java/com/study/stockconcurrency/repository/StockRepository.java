package com.study.stockconcurrency.repository;

import com.study.stockconcurrency.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StockRepository extends JpaRepository<Stock, Long> {

}
