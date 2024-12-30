package com.example.auth.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.auth.model.UserInfo;

@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Integer> {

    // Find user by username
    Optional<UserInfo> findByUsername(String username);

    // Find user by email
    Optional<UserInfo> findByEmail(String email);
    
    // Add more custom query methods if needed
}
