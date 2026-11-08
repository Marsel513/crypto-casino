package com.crypto.casino.auth;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginRequest {

    @NotBlank(message = "Password is required")
    @Size(max = 72, message = "Password is too long")
    private String password;

    @NotBlank(message = "Email is required")
    @Size(max = 255, message = "Email is too long")
    private String email;
}
