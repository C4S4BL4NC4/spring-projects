package com.java.springgateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        // Auth server endpoints must be reachable without a token so clients can get one
                        .requestMatchers("/oauth2/**", "/.well-known/**").permitAll()
                        .anyRequest().authenticated())
                // Validate bearer JWTs issued by the auth server (issuer-uri in application.yaml)
                .oauth2ResourceServer(oauth2ResourceServer -> oauth2ResourceServer
                        .jwt(Customizer.withDefaults()))
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
