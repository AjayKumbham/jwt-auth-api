package com.example.auth.filter;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.auth.service.CookieJwtService;
import com.example.auth.service.JwtService;
import com.example.auth.service.UserInfoService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CookieJwtService cookieJwtService;

    @Autowired
    private UserInfoService userDetailsService;

    @Override
    @SuppressWarnings("UseSpecificCatch")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // Extract JWT token from HTTP-only cookie
        Optional<String> tokenOptional = cookieJwtService.extractTokenFromCookie(request);
        String token = null;
        String username = null;
        
        if (tokenOptional.isPresent()) {
            token = tokenOptional.get();
            try {
                username = jwtService.extractUsername(token);
            } catch (Exception e) {
                log.warn("Failed to extract username from JWT token: {}", e.getMessage());
            }
        }

        // If the token is valid and no authentication is set in the context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Validate token and set authentication
                if (jwtService.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    log.debug("Authentication set for user: {}", username);
                } else {
                    log.warn("Invalid JWT token for user: {}", username);
                }
            } catch (Exception e) {
                // Simple error handling for unexpected issues
                log.error("Error validating token for user: {}", username, e);
            }
        } 

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
