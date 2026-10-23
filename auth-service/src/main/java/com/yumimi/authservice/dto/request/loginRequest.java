package com.yumimi.authservice.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class loginRequest {

    @NotBlank( message = "Username must be blank")
    private String username;

    @NotBlank(message = "Password must be blank")
    private String password;
}
