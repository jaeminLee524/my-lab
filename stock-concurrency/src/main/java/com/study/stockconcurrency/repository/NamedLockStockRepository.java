package com.study.stockconcurrency.repository;

import com.study.stockconcurrency.entity.NamedStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface NamedLockStockRepository extends JpaRepository<NamedStock, Long> {

    @Query(value = "SELECT GET_LOCK(:key, 3000)", nativeQuery = true)
    int getLock(String key);

    @Query(value = "SELECT RELEASE_LOCK(:key)", nativeQuery = true)
    int releaseLock(String key);
}
