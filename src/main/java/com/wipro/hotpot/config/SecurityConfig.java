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

                // ✅ 1. PUBLIC — no token needed at all
                .requestMatchers(
                    "/api/auth/register",
                    "/api/auth/login",
                    "/api/auth/isEmailExists/**",
                    "/api/menu/search",
                    "/api/menu/restaurant/**",
                    "/api/menu/available/**",
                    "/api/menu/category/**",
                    "/api/menu/filter",
                    "/api/restaurant/all",
                    "/api/restaurant/active",
                    "/api/restaurant/search",
                    "/swagger-ui/**",
                    "/swagger-ui.html",
                    "/v3/api-docs/**",
                    "/api-docs/**"
                ).permitAll()

                // ✅ 2. ADMIN only routes
                .requestMatchers(
                    "/api/auth/users",
                    "/api/restaurant/add",
                    "/api/restaurant/delete/**"
                ).hasRole("ADMIN")

                // ✅ 3. RESTAURANT only routes
                .requestMatchers(
                    "/api/menu/add",
                    "/api/menu/update/**",
                    "/api/menu/delete/**",
                    "/api/menu/outofstock/**",
                    "/api/restaurant/update/**"
                ).hasRole("RESTAURANT")

                // ✅ 4. USER only routes
                .requestMatchers(
                    "/api/cart/**",
                    "/api/order/place/**",
                    "/api/order/history/**",
                    "/api/order/cancel/**",
                    "/api/order/user/**"
                ).hasRole("USER")

                // ✅ 5. Any logged in user can access
                // (USER + RESTAURANT + ADMIN)
                .requestMatchers(
                    "/api/order/**",
                    "/api/tracking/**",
                    "/api/auth/user/**",
                    "/api/restaurant/**",
                    "/api/menu/**"
                ).authenticated()

                // ✅ 6. Everything else needs login
                .anyRequest().authenticated()
            )

            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

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
