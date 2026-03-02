package com.wipro.hotpot.config;

import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import com.wipro.hotpot.entity.User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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

        try {
            // ✅ Step 1 - Get Authorization header
            String authHeader = request.getHeader("Authorization");

            // ✅ DEBUG - print what we received
            System.out.println("=== JWT FILTER ===");
            System.out.println("Request URL : " + request.getRequestURI());
            System.out.println("Auth Header : " + authHeader);

            String token = null;
            String email = null;

            // ✅ Step 2 - Extract token from header
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                token = authHeader.substring(7).trim(); // trim any spaces
                System.out.println("Token found : " + token.substring(0, 20) + "...");

                try {
                    email = jwtUtil.extractEmail(token);
                    System.out.println("Email extracted : " + email);
                } catch (Exception e) {
                    System.out.println("Token extraction failed: " + e.getMessage());
                }
            } else {
                System.out.println("No Bearer token found in header!");
            }

            // ✅ Step 3 - Validate and set authentication
            if (email != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

                User user = userRepository.findByEmail(email).orElse(null);

                if (user != null) {
                    System.out.println("User found: " + user.getEmail());

                    if (jwtUtil.isTokenValid(token)) {
                        System.out.println("Token is valid!");

                        String role = jwtUtil.extractRole(token);
                        System.out.println("Role: " + role);

                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(
                                        email,
                                        null,
                                        Collections.singletonList(
                                                new SimpleGrantedAuthority("ROLE_" + role)
                                        )
                                );

                        authToken.setDetails(
                                new WebAuthenticationDetailsSource()
                                        .buildDetails(request)
                        );

                        SecurityContextHolder.getContext()
                                .setAuthentication(authToken);

                        System.out.println("Authentication set successfully!");

                    } else {
                        System.out.println("Token is INVALID or EXPIRED!");
                    }
                } else {
                    System.out.println("User NOT found in database!");
                }
            }

        } catch (Exception e) {
            System.out.println("JwtFilter error: " + e.getMessage());
            e.printStackTrace();
        }

        // ✅ Step 4 - Continue filter chain
        filterChain.doFilter(request, response);
    }
}
