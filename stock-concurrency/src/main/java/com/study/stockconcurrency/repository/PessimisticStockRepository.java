package com.study.stockconcurrency.repository;

import com.study.stockconcurrency.entity.PessimisticStock;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface PessimisticStockRepository extends JpaRepository<PessimisticStock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PessimisticStock> findById(@Param("id") Long id);

    @Query("select s from PessimisticStock s where s.id = :id")
    Optional<PessimisticStock> find(@Param("id") Long id);
}
