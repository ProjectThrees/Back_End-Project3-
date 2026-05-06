package com.studentmarketplace.backend.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * Full application tests need a {@link JwtDecoder} bean; production relies on
 * issuer/JWK configuration that is not available in CI.
 */
@TestConfiguration
public class TestJwtDecoderConfig {

    private static final String TEST_SECRET = "test-jwt-secret-must-be-at-least-32-chars-long";

    @Bean
    @Primary
    JwtDecoder jwtDecoder() {
        SecretKey secretKey = new SecretKeySpec(TEST_SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}
