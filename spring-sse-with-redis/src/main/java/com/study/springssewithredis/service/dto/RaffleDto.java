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
    public static class RaffleResponse {

        private Long id;
        private Double winnerProbability;

        public static RaffleResponse from(Raffle raffle, Long participationCount) {
            return RaffleResponse.builder()
                .id(raffle.getId())
                .winnerProbability((double) 1 / participationCount * 100)
                .build();
        }
    }
}
