package com.study.stockconcurrency.repository;

import com.study.stockconcurrency.entity.OptimisticStock;
import com.study.stockconcurrency.entity.PessimisticStock;
import io.lettuce.core.dynamic.annotation.Param;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

/**
 * LockModeType.OPTIMISTIC
 * LockModeType.READ 옵션과 동일하다.
 * 해당 옵션으로 조회된
 *      엔티티가 변경되면 업데이트 시점에 버전 값이 증가한다.
 *      엔티티 변경이 없다면 버전 값은 증가하지 않는다.
 */
public interface OptimisticStockRepository extends JpaRepository<OptimisticStock, Long> {

    @Lock(value = LockModeType.OPTIMISTIC)
    @Query("select s from OptimisticStock s where s.id = :id")
    OptimisticStock findByIdWithOptimisticLock(Long id);
}
