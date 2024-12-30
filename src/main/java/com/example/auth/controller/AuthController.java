package com.example.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth.model.AuthRequest;
import com.example.auth.model.UserInfo;
import com.example.auth.service.JwtService;
import com.example.auth.service.UserInfoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserInfoService service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @GetMapping("/welcome")
    public ResponseEntity<String> welcome() {
        return ResponseEntity.ok("Welcome, this endpoint is not secure.");
    }

    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@Valid @RequestBody UserInfo userInfo) {
        String response = service.addUser(userInfo);
        return new ResponseEntity<>(response, HttpStatus.CREATED); // Return 201 for successful registration
    }

    @GetMapping("/user/user-profile")
    @PreAuthorize("hasRole('ROLE_USER')") // Only allows access if the user has the 'USER' role
    public ResponseEntity<String> userProfile() {
        return ResponseEntity.ok("Welcome to User Profile"); // Returns a welcome message for the User Profile
    }
    
    @GetMapping("/admin/admin-profile")
    @PreAuthorize("hasRole('ROLE_ADMIN')") // Only allows access if the user has the 'ADMIN' role
    public ResponseEntity<String> adminProfile() {
        return ResponseEntity.ok("Welcome to Admin Profile"); // Returns a welcome message for the Admin Profile
    }
    
    @PostMapping("/login")
    public ResponseEntity<String> authenticateAndGetToken(@Valid @RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );
        if (authentication.isAuthenticated()) {
            String token = jwtService.generateToken(authRequest.getUsername());
            return ResponseEntity.ok(token); // Return the JWT token with a 200 status
        } else {
            // Return a proper error response if authentication fails
            return new ResponseEntity<>("Invalid user credentials", HttpStatus.UNAUTHORIZED);
        }
    }
}
