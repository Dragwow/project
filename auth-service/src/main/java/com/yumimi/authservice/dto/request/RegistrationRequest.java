package com.yumimi.authservice.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 30)
    @Pattern(
            regexp = "^[a-zA-Z0-9_]+$",
            message = "Username may context only letters, digits and underscore")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "First Name is required")
    @Size(max = 100)
    private String firstName;

    @NotBlank(message = "Last Name is required")
    @Size(max = 100)
    private String lastName;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 73)
    private String password;

    @NotBlank(message = "Second password is required")
    @Size(min = 8, max = 73)
    private String secondPassword;


}
