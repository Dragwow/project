
package com.yumimi.userservice.config;

import com.yumimi.eventsservice.events.UserCreateEvent;
import com.yumimi.userservice.dto.request.UserCreateRequest;
import com.yumimi.userservice.facade.UserFacade;
import com.yumimi.userservice.utils.UserCreateResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import static com.yumimi.eventsservice.topics.StaticTopicsUsers.TOPIC_CREATE_USER;
import static com.yumimi.eventsservice.topics.StaticTopicsUsers.USER_GROUP;
import static com.yumimi.utilsservice.StaticString.StringsGateway.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserConsumer {

    private final UserFacade userFacade;

    @KafkaListener(topics = TOPIC_CREATE_USER, groupId = USER_GROUP)
    public void userCreate(
            UserCreateEvent event,
            @Header(name = HEADER, required = false) String requestId) {
        if (requestId != null){
            MDC.put(MDC_KEY, requestId);
        }

        try {
            log.info("Kafka event received: userId={}", event.getUserId());

            UserCreateResult result = userFacade.create(new UserCreateRequest(
                    event.getUserId(),
                    event.getUsername(),
                    event.getEmail(),
                    event.getFirstName(),
                    event.getLastName()
            ));

            switch(result){
                case CREATED -> log.info("User profile created: userId={}", event.getUserId());
                case SKIPPED_RESULT -> log.info("User profile skipped due to duplicate: userId={}", event.getUserId());
            }
        }finally {
            MDC.remove(MDC_KEY);
        }


    }
}
