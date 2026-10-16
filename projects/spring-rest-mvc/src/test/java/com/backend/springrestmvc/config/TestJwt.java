package com.backend.springrestmvc.config;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.JwtRequestPostProcessor;

import java.time.Instant;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public final class TestJwt {

    // Mock bearer token for MockMvc requests, so tests don't need a running auth server
    public static final JwtRequestPostProcessor JWT = jwt().jwt(jwt -> jwt
            .claims(claims -> claims.put("scope", "message.read message.write"))
            .subject("messaging-client")
            .notBefore(Instant.now().minusSeconds(5)));

    private TestJwt() {
    }
}
