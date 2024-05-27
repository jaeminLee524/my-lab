package com.study.springssewithredis.interfaces;

import com.study.springssewithredis.service.RaffleSubscribeService;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/")
public class RaffleController {

    private final RaffleSubscribeService raffleSubscribeService;

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    public SseEmitter subscribe(@RequestParam("id") Long raffleId) throws IOException {
        return raffleSubscribeService.subscribe(raffleId);
    }
}
