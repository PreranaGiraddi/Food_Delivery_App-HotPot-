package com.wipro.hotpot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    // ✅ All Swagger + public URLs
    private static final String[] PUBLIC_URLS = {
        // Swagger
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/v3/api-docs/**",
        "/v3/api-docs",
        "/swagger-resources/**",
        "/webjars/**",
        "/configuration/**",
        "/configuration/ui",
        "/configuration/security",
        // Auth
        "/api/auth/**",
        // Static HTML pages
        "/",
        "/*.html",
        "/index.html",
        "/login.html",
        "/register.html",
        "/css/**",
        "/js/**",
        "/images/**",
        "/favicon.ico",
        // Error
        "/error"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())

            // ✅ Disable frame options for Swagger UI
            .headers(headers -> headers.frameOptions(frame -> frame.disable()))

            .authorizeHttpRequests(auth -> auth

                // ✅ Swagger and public — MUST be FIRST
                .requestMatchers(PUBLIC_URLS).permitAll()

                // ✅ Public GET endpoints (no login needed)
                .requestMatchers(HttpMethod.GET,
                    "/api/restaurant/active",
                    "/api/restaurant/search",
                    "/api/restaurant/all",
                    "/api/menu/available/**",
                    "/api/menu/restaurant/**",
                    "/api/category/**"
                ).permitAll()

                // ✅ Everything else needs authentication
                .anyRequest().authenticated()
            )

            .sessionManagement(sess ->
                sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .addFilterBefore(jwtFilter,
                UsernamePasswordAuthenticationFilter.class);

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