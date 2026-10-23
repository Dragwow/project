
package com.yumimi.userservice.config;

import com.yumimi.eventsservice.events.UserCreateEvent;
import com.yumimi.userservice.dto.request.UserCreateRequest;
import com.yumimi.userservice.facade.UserFacade;
import com.yumimi.userservice.utils.UserCreateResult;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.yumimi.eventsservice.topics.StaticTopicsUsers.TOPIC_CREATE_USER;

@Slf4j
@Service
@AllArgsConstructor
public class UserConsumer {

    private final UserFacade userFacade;

    @KafkaListener(
            topics = TOPIC_CREATE_USER,
            groupId = "user-group"
    )
    public void userCreate(UserCreateEvent event) {
        log.info("Kafka event received: userId={}", event.getUserId());

        UserCreateRequest request = new UserCreateRequest(
                event.getUserId(),
                event.getUsername(),
                event.getEmail(),
                event.getFirstName(),
                event.getLastName()
        );

        UserCreateResult result = userFacade.create(request);
        switch(result){
            case CREATED ->
                log.info("User profile created: userId={}", event.getUserId());
            case SKIPPED_RESULT ->
                log.info("User profile skipped due to duplicate: userID={}", event.getUserId());
        }

    }
}
