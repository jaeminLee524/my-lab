package com.study.springssewithredis.infrastructure;

import com.study.springssewithredis.domain.Participation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("SELECT COUNT(r) FROM Participation r WHERE r.raffleId = :raffleId")
    Long countParticipationByRaffleId(Long raffleId);
}
