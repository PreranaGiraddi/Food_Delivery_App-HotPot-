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

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .authorizeHttpRequests(auth -> auth
                // PUBLIC routes
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/isEmailExists/**",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**"
                ).permitAll()

                // ADMIN only
                .requestMatchers(
                    "/api/auth/users",
                    "/api/auth/user/**"
                ).hasRole("ADMIN")

                // RESTAURANT only
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

                // USER only
                .requestMatchers(
                    "/api/cart/**",
                    "/api/order/place/**",
                    "/api/order/history/**",
                    "/api/order/cancel/**",
                    "/api/tracking/user/**"
                ).hasRole("USER")

                // Any other route requires authentication
                .anyRequest().authenticated()
            )
            // JWT-based stateless session
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            // Add JWT filter before UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}