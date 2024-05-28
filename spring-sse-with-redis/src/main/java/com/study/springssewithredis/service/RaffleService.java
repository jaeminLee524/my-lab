package com.study.springssewithredis.service;

import com.study.springssewithredis.domain.Participation;
import com.study.springssewithredis.domain.Raffle;
import com.study.springssewithredis.infrastructure.ParticipationRepository;
import com.study.springssewithredis.infrastructure.RaffleRepository;
import com.study.springssewithredis.service.dto.RaffleDto.RaffleParticipationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RaffleService {

    private final RaffleRepository raffleRepository;
    private final ParticipationRepository participationRepository;
    private final RedisOperations<String, RaffleParticipationRequest> redisOperations;

    /**
     * 래플 참여
     * 래플 참여 시 래플 참여 정보를 저장하고, 래플 정보를 Redis로 Publish 한다.
     *
     * @param raffleId
     * @param memberId
     */
    public void participateInRaffle(Long raffleId, Long memberId) {
        Raffle raffle = raffleRepository.findById(raffleId)
            .orElseThrow(() -> new IllegalArgumentException("해당하는 래플이 존재하지 않습니다."));

        participationRepository.save(Participation.builder()
            .raffleId(raffle.getId())
            .memberId(memberId)
            .build());

        redisOperations.convertAndSend(
            RaffleChannelGenerator.getChannelName(String.valueOf(raffle.getId())),
            RaffleParticipationRequest.of(raffle, memberId)
        );
    }
}
