package com.yumimi.eventsservice.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateEvent {

    private Long userId;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
}
