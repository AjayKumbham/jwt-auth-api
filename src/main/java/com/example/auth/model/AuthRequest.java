package com.example.auth.model;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // for validation

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequest {

    @NotEmpty(message = "Username cannot be empty")  // Validation for non-empty username
    private String username;

    @NotEmpty(message = "Password cannot be empty")  // Validation for non-empty password
    private String password;

}
