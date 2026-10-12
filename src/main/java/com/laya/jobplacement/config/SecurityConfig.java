package com.laya.jobplacement.config;

import com.laya.jobplacement.security.JwtAuthFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
            .csrf(csrf -> csrf.disable())

            .cors(cors -> {})

            .sessionManagement(session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS
                )
            )

            .authorizeHttpRequests(auth -> auth

                // Registration and Login
                .requestMatchers("/api/users/**")
                    .permitAll()

                // Student APIs
                .requestMatchers("/api/students/**")
                    .hasAnyRole("STUDENT", "ADMIN")

                // Company APIs
                .requestMatchers("/api/companies/**")
                    .hasAnyRole("RECRUITER", "ADMIN")

                // Job APIs
                // All logged-in users can view/manage through controller rules
                .requestMatchers("/api/jobs/**")
                    .authenticated()

                // Dashboard
                .requestMatchers("/api/dashboard/**")
                    .authenticated()

                // Application APIs
                .requestMatchers("/api/applications/**")
                    .hasAnyRole("STUDENT", "RECRUITER", "ADMIN")
.requestMatchers("/api/matching/**")
    .hasAnyRole("STUDENT", "ADMIN")
                // Everything else
                .anyRequest()
                    .authenticated()
            )

            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
    @Bean
public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();

    configuration.setAllowedOriginPatterns(java.util.List.of(
        "http://localhost:5173",
        "http://localhost:5174",
        "https://*.onrender.com"
    ));

    configuration.setAllowedMethods(java.util.List.of(
        "GET", "POST", "PUT", "DELETE", "OPTIONS"
    ));

    configuration.setAllowedHeaders(java.util.List.of("*"));
    configuration.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source =
        new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", configuration);

    return source;
}
}