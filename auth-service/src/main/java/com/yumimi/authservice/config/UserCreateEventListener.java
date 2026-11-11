package com.yumimi.authservice.config;

import com.yumimi.eventsservice.events.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserCreateEventListener {

    private final AuthentificationProducer producer;

    @TransactionalEventListener
    public void onUserCreate(UserCreateEvent event){
        log.info("Sending UserCreateEvent to Kafka, userId={}", event.getUserId());
        producer.sendUserCreatingTemplate(event);
    }
}
