package com.example.auth.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.auth.model.UserInfo;

public class UserInfoDetails implements UserDetails {

    private final String username; 
    private final String password;
    private final List<GrantedAuthority> authorities;

    // Constructor that initializes UserInfoDetails based on a UserInfo entity
    public UserInfoDetails(UserInfo userInfo) {
        this.username = userInfo.getUsername();
        this.password = userInfo.getPassword();
        
        // Split roles by comma, trim spaces, and convert to a list of SimpleGrantedAuthority objects
        this.authorities = List.of(userInfo.getRoles().split(","))
                .stream()
                .map(role -> new SimpleGrantedAuthority(role.trim())) // Trim spaces in case of malformed input
                .collect(Collectors.toList());
    }

    // Return a collection of GrantedAuthority objects representing the roles/permissions
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    // Return the user's password
    @Override
    public String getPassword() {
        return password;
    }

    // Return the user's username
    @Override
    public String getUsername() {
        return username;
    }

    // Account is not expired by default, implement custom logic if needed
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // Account is not locked by default, implement custom logic if needed
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // Credentials are not expired by default, implement custom logic if needed
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // User is enabled by default, implement custom logic if needed
    @Override
    public boolean isEnabled() {
        return true;
    }

    // Optionally, override equals and hashCode if needed for consistency in collections
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInfoDetails that = (UserInfoDetails) o;
        return username.equals(that.username);
    }

    @Override
    public int hashCode() {
        return username.hashCode();
    }
}
