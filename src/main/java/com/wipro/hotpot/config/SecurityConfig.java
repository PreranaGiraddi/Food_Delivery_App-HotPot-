package com.wipro.hotpot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // ✅ Define which routes are PUBLIC and which are PROTECTED
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // Disable CSRF (not needed for REST APIs)
            .csrf(csrf -> csrf.disable())

            // Define route permissions
            .authorizeHttpRequests(auth -> auth

                // ✅ PUBLIC routes — anyone can access
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/isEmailExists/**",
                    "/api/menu/search",
                    "/api/menu/restaurant/**",
                    "/api/menu/available/**",
                    "/api/menu/filter",
                    "/api/restaurant/all",
                    "/api/restaurant/active",
                    "/api/restaurant/search",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/api-docs/**"
                ).permitAll()

                // ✅ ADMIN only routes
                .requestMatchers(
                    "/api/auth/users",
                    "/api/auth/user/**",
                    "/api/restaurant/add",
                    "/api/restaurant/delete/**"
                ).hasRole("ADMIN")

                // ✅ RESTAURANT only routes
                .requestMatchers(
                    "/api/menu/add",
                    "/api/menu/update/**",
                    "/api/menu/delete/**",
                    "/api/menu/outofstock/**",
                    "/api/restaurant/update/**",
                    "/api/order/restaurant/**",
                    "/api/tracking/update",
                    "/api/tracking/restaurant/**"
                ).hasRole("RESTAURANT")

                // ✅ USER only routes
                .requestMatchers(
                    "/api/cart/**",
                    "/api/order/place/**",
                    "/api/order/history/**",
                    "/api/order/cancel/**",
                    "/api/tracking/user/**"
                ).hasRole("USER")

                // ✅ Any other route needs authentication
                .anyRequest().authenticated()
            )

            // Use stateless session (JWT based)
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            // Add JWT filter before Spring's default auth filter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // ✅ Password encoder — BCrypt hashing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // ✅ Authentication manager
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
