package com.example.auth.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth.model.UserInfo;
import com.example.auth.repository.UserInfoRepository;

import jakarta.mail.MessagingException;

@Service
public class UserInfoService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserInfoService.class);

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private EmailService emailService;

    // Load user by username
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByUsername(username);

        // If user is found, convert to UserDetails, otherwise throw exception
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> {
                    logger.error("User not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });
    }

    // Add new user to the system
    @Transactional(rollbackFor = Exception.class)
    public String addUser(UserInfo userInfo) {
        try {
            // Check if the username already exists
            if (repository.findByUsername(userInfo.getUsername()).isPresent()) {
                logger.warn("User already exists: {}", userInfo.getUsername());
                return "Username already taken";
            }

            // Encode the password before saving the user
            userInfo.setPassword(encoder.encode(userInfo.getPassword()));

            // Save the user in the database
            repository.save(userInfo);

            // Send welcome email
            emailService.sendWelcomeEmail(userInfo.getEmail(), userInfo.getUsername());
            logger.info("User added successfully: {}", userInfo.getUsername());
            return "User Added Successfully";

        } catch (DataIntegrityViolationException e) {
            // Handle database integrity issues (e.g., duplicate record)
            logger.error("Data integrity violation: {}", e.getMessage(), e);
            return "Data integrity violation, possibly a duplicate record";
        } catch (MessagingException e) {
            // Handle issues with sending emails
            logger.error("Error occurred while sending welcome email: {}", e.getMessage(), e);
            return "Error occurred while sending the welcome email: " + e.getMessage();
        } catch (Exception e) {
            // Catch any other unforeseen errors
            logger.error("Unexpected error: {}", e.getMessage(), e);
            return "An unexpected error occurred. Please try again later.";
        }
    }
}
