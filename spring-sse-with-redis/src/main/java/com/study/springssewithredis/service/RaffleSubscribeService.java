package com.study.springssewithredis.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.study.springssewithredis.infrastructure.ParticipationRepository;
import com.study.springssewithredis.service.dto.RaffleDto.RaffleParticipationRequest;
import com.study.springssewithredis.service.dto.RaffleDto.RaffleResponse;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Slf4j
@RequiredArgsConstructor
@Service
public class RaffleSubscribeService {

    private static final long DEFAULT_TIMEOUT = 60L * 1000 * 60;
    private static final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();
    public static final String RAFFLE_EVENT_NAME = "raffle-winning-probability";
    private final ObjectMapper objectMapper;
    private final ParticipationRepository participationRepository;
    private final RedisMessageListenerContainer redisMessageListenerContainer;

    public SseEmitter subscribe(final Long raffleId) throws IOException {
        final String id = String.valueOf(raffleId);
        final SseEmitter emitter = createSseEmitter(id);
        final MessageListener messageListener = createMessageListenerAndSendToClient(emitter, id);

        addMessageListenerToContainer(messageListener, id);

        handleEmitterCompletionAndTimeout(emitter, messageListener);

        return emitter;
    }

    /**
     * SSE 연결을 생성합니다.
     *
     * @param id
     * @return
     * @throws IOException
     */
    private SseEmitter createSseEmitter(String id) throws IOException {
        final SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);

        emitter.send(
            SseEmitter.event()
                .id(id)
                .name(RAFFLE_EVENT_NAME)
        );
        emitters.add(emitter);
        return emitter;
    }

    /**
     * redis 메시지 리스너를 생성합니다.
     *
     * @param emitter
     * @param id
     * @return
     */
    private MessageListener createMessageListenerAndSendToClient(SseEmitter emitter, String id) {
        final MessageListener messageListener = (message, pattern) -> {
            RaffleResponse raffleResponse = convertFrom(message);

            sendToClient(emitter, id, raffleResponse);
        };
        return messageListener;
    }

    private RaffleResponse convertFrom(Message message) {
        try {
            final RaffleParticipationRequest raffle = this.objectMapper.readValue(message.getBody(), RaffleParticipationRequest.class);

            final Long participationCount = participationRepository.countParticipationByRaffleId(raffle.getRaffleId());

            return RaffleResponse.from(raffle, participationCount);
        } catch (IOException e) {
            throw new RuntimeException(message.toString());
        }
    }

    private void sendToClient(final SseEmitter emitter, final String id, final Object data) {
        try {
            emitter.send(SseEmitter.event()
                .id(id)
                .name(RAFFLE_EVENT_NAME)
                .data(data));
        } catch (IOException e) {
            emitters.remove(emitter);
            log.error("SSE 연결이 올바르지 않습니다. 해당 raffleID={}", id);
        }
    }

    /**
     * 메시지 리스너를 컨테이너에 추가합니다.
     *
     * @param messageListener
     * @param id
     */
    private void addMessageListenerToContainer(MessageListener messageListener, String id) {
        this.redisMessageListenerContainer.addMessageListener(messageListener, ChannelTopic.of(RaffleChannelGenerator.getChannelName(id)));
    }

    private void handleEmitterCompletionAndTimeout(final SseEmitter emitter, final MessageListener messageListener) {
        emitter.onCompletion(() -> {
            emitters.remove(emitter);
            this.redisMessageListenerContainer.removeMessageListener(messageListener);
        });
        emitter.onTimeout(() -> {
            emitters.remove(emitter);
            this.redisMessageListenerContainer.removeMessageListener(messageListener);
        });
    }
}
