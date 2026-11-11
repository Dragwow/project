package com.yumimi.authservice.config;

import com.yumimi.eventsservice.events.UserCreateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.MDC;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;

import static com.yumimi.eventsservice.topics.StaticTopicsUsers.TOPIC_CREATE_USER;
import static com.yumimi.utilsservice.StaticString.StringsGateway.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthentificationProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendUserCreatingTemplate(UserCreateEvent event){
        String key = String.valueOf(event.getUserId());
        String requestId = MDC.get(MDC_KEY);

        ProducerRecord<String, Object> record =
                new ProducerRecord<>(TOPIC_CREATE_USER, key, event);
        if (requestId != null){
            record.headers().add(HEADER, requestId.getBytes(StandardCharsets.UTF_8));
        }

        kafkaTemplate.send(record).whenComplete((result, exception) ->{
            if (requestId != null) MDC.put(MDC_KEY, requestId);
            try {
               if (exception != null){
                   log.error("Failed to publish UserCreateEvent, userId={}", event.getUserId());
               }else {
                   log.info("UserCreateEvent published userId={}", key);
               }
            }finally {
                MDC.remove(MDC_KEY);
            }
        });

    }
}
