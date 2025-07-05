package com.example.auth.service;

import java.time.Duration;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class CookieJwtService {

    @Value("${jwt.cookie.name:jwt-token}")
    private String cookieName;

    @Value("${jwt.cookie.max-age:1800}")
    private int cookieMaxAge; // 30 minutes in seconds

    @Value("${jwt.cookie.secure:true}")
    private boolean cookieSecure;

    @Value("${jwt.cookie.http-only:true}")
    private boolean cookieHttpOnly;

    @Value("${jwt.cookie.same-site:Strict}")
    private String cookieSameSite;

    /**
     * Creates an HTTP-only cookie containing the JWT token
     * @param token JWT token to store in cookie
     * @return ResponseCookie configured with security settings
     */
    public ResponseCookie createJwtCookie(String token) {
        return ResponseCookie.from(cookieName, token)
                .maxAge(Duration.ofSeconds(cookieMaxAge))
                .path("/")
                .secure(cookieSecure)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .build();
    }

    /**
     * Creates a cookie to clear the JWT token (logout)
     * @return ResponseCookie that clears the JWT cookie
     */
    public ResponseCookie createLogoutCookie() {
        return ResponseCookie.from(cookieName, "")
                .maxAge(Duration.ZERO)
                .path("/")
                .secure(cookieSecure)
                .httpOnly(cookieHttpOnly)
                .sameSite(cookieSameSite)
                .build();
    }

    /**
     * Extracts JWT token from HTTP-only cookie
     * @param request HTTP request containing cookies
     * @return Optional containing the JWT token if present
     */
    public Optional<String> extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        for (Cookie cookie : request.getCookies()) {
            if (cookieName.equals(cookie.getName())) {
                String token = cookie.getValue();
                if (token != null && !token.isEmpty()) {
                    return Optional.of(token);
                }
            }
        }
        return Optional.empty();
    }

    /**
     * Adds JWT cookie to HTTP response
     * @param response HTTP response to add cookie to
     * @param token JWT token to store
     */
    public void addJwtCookieToResponse(HttpServletResponse response, String token) {
        ResponseCookie cookie = createJwtCookie(token);
        response.addHeader("Set-Cookie", cookie.toString());
    }

    /**
     * Clears JWT cookie from HTTP response (logout)
     * @param response HTTP response to clear cookie from
     */
    public void clearJwtCookieFromResponse(HttpServletResponse response) {
        ResponseCookie cookie = createLogoutCookie();
        response.addHeader("Set-Cookie", cookie.toString());
    }
} 