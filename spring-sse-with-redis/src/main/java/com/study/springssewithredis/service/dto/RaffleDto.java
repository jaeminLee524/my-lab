package com.study.springssewithredis.service.dto;

import com.study.springssewithredis.domain.Raffle;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


public class RaffleDto {

    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RaffleParticipationRequest {

        private Long raffleId;
        private String raffleName;
        private Long memberId;

        public static RaffleParticipationRequest of(Raffle raffle, Long memberId) {
            return RaffleParticipationRequest.builder()
                .raffleId(raffle.getId())
                .raffleName(raffle.getName())
                .memberId(memberId)
                .build();
        }
    }


    @Getter
    @Builder
    @ToString
    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class RaffleResponse {

        private Long raffleId;
        private String raffleName;
        private Double winnerProbability;

        public static RaffleResponse from(RaffleParticipationRequest raffle, Long participationCount) {
            return RaffleResponse.builder()
                .raffleId(raffle.getRaffleId())
                .raffleName(raffle.getRaffleName())
                .winnerProbability((double) 1 / participationCount * 100)
                .build();
        }
    }
}
