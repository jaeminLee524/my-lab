package com.study.springssewithredis.infrastructure;

import com.study.springssewithredis.domain.Raffle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RaffleRepository extends JpaRepository<Raffle, Long> {

}
