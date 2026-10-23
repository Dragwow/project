package com.yumimi.authservice.config;

import com.yumimi.eventsservice.events.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

import static com.yumimi.eventsservice.topics.StaticTopicsUsers.TOPIC_CREATE_USER;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthentificationProducer {

    private final KafkaTemplate<String, UserCreateEvent> userCreateEventKafkaTemplate;

    public CompletableFuture<SendResult<String, UserCreateEvent>>
    sendUserCreatingTemplate(UserCreateEvent event){
        return userCreateEventKafkaTemplate
                .send(TOPIC_CREATE_USER, event.getUserId().toString(), event)
                .whenComplete((result, exception) -> {
                    if (exception != null) {
                        log.error("Failed to publish UserCreateEvent", exception);
                    }else {
                        log.info(
                                "UserCreateEvent published for userId={}",
                                event.getUserId()
                        );
                    }
                });
    }
}
