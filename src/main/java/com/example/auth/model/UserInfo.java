package com.example.auth.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    @NotEmpty(message = "Username cannot be empty")
    private String username;

    @Column(unique = true, nullable = false)
    @Email(message = "Invalid email format")  // Ensures that the email is properly formatted
    @NotEmpty(message = "Email cannot be empty")
    private String email;

    @NotEmpty(message = "Password cannot be empty")  // Ensures password is not empty
    private String password;

    @Column(nullable = false)
    @NotEmpty(message = "Roles cannot be empty")  // Ensures roles are not empty
    private String roles;
}
