package com.study.springssewithredis.interfaces;

import com.study.springssewithredis.service.RaffleService;
import com.study.springssewithredis.service.RaffleSubscribeService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/raffle")
public class RaffleController {

    private final RaffleSubscribeService raffleSubscribeService;
    private final RaffleService raffleService;

    @GetMapping(value = "/{id}/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable("id") Long raffleId) throws IOException {
        return raffleSubscribeService.subscribe(raffleId);
    }

    @PostMapping("/{id}")
    public void participateInRaffle(
        @PathVariable("id") Long raffleId,
        @RequestParam("memberId") Long memberId
    ) {
        raffleService.participateInRaffle(raffleId, memberId);
    }
}
