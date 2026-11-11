package com.yumimi.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotBlank( message = "Username is required")
    @Size(min = 3, max = 30)
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 72)
    private String password;

}
