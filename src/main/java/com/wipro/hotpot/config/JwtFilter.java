package com.wipro.hotpot.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.wipro.hotpot.entity.User;
import com.wipro.hotpot.repository.IUserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private IUserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // ✅ Step 1 - Get Authorization header from request
        String authHeader = request.getHeader("Authorization");

        String token = null;
        String email = null;

        // ✅ Step 2 - Check if header starts with "Bearer "
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7); // remove "Bearer " prefix
            email = jwtUtil.extractEmail(token);
        }

        // ✅ Step 3 - Validate token and set authentication
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Find user from database
            User user = userRepository.findByEmail(email).orElse(null);

            if (user != null && jwtUtil.isTokenValid(token)) {

                // Extract role from token
                String role = jwtUtil.extractRole(token);

                // Create authentication token with role
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                email,
                                null,
                                Collections.singletonList(
                                        new SimpleGrantedAuthority("ROLE_" + role)
                                )
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // ✅ Step 4 - Continue with filter chain
        filterChain.doFilter(request, response);
    }
}

