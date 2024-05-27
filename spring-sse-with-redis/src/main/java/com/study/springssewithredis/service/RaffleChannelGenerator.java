package com.study.springssewithredis.service;

public class RaffleChannelGenerator {

    public static String getChannelName(String raffleId) {
        return "raffle:topics:" + raffleId;
    }
}
