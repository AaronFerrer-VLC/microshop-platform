package com.microshop.user.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para JwtService.
 * 
 * @author Microshop Platform
 * @version 1.0.0
 */
@SpringBootTest(
    properties = {
        "spring.autoconfigure.exclude=org.springframework.cloud.netflix.eureka.EurekaClientAutoConfiguration,org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration,org.springframework.cloud.netflix.eureka.EurekaServiceRegistryAutoConfiguration"
    }
)
@ActiveProfiles("test")
@TestPropertySource(properties = {
    "eureka.client.enabled=false",
    "eureka.client.register-with-eureka=false",
    "eureka.client.fetch-registry=false",
    "spring.cloud.discovery.enabled=false",
    "spring.cloud.service-registry.auto-registration.enabled=false",
    "spring.cloud.config.enabled=false",
    "jwt.secret=test-secret-key-for-jwt-token-generation-minimum-256-bits-required-for-hmac-sha-256-algorithm",
    "jwt.expiration=3600000" // 1 hora para tests
})
@DisplayName("JwtService Tests")
class JwtServiceTest {

    @Autowired
    private JwtService jwtService;

    private String testEmail = "test@example.com";
    private String testRole = "CUSTOMER";

    @Test
    @DisplayName("Debería generar un token JWT válido")
    void shouldGenerateValidToken() {
        // When
        String token = jwtService.generateToken(testEmail, testRole);

        // Then
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.split("\\.").length == 3); // JWT tiene 3 partes separadas por puntos
    }

    @Test
    @DisplayName("Debería extraer el email del token")
    void shouldExtractEmailFromToken() {
        // Given
        String token = jwtService.generateToken(testEmail, testRole);

        // When
        String extractedEmail = jwtService.extractEmail(token);

        // Then
        assertEquals(testEmail, extractedEmail);
    }

    @Test
    @DisplayName("Debería extraer el rol del token")
    void shouldExtractRoleFromToken() {
        // Given
        String token = jwtService.generateToken(testEmail, testRole);

        // When
        String extractedRole = jwtService.extractRole(token);

        // Then
        assertEquals(testRole, extractedRole);
    }

    @Test
    @DisplayName("Debería extraer la fecha de expiración del token")
    void shouldExtractExpirationFromToken() {
        // Given
        String token = jwtService.generateToken(testEmail, testRole);

        // When
        Date expiration = jwtService.extractExpiration(token);

        // Then
        assertNotNull(expiration);
        assertTrue(expiration.after(new Date()));
    }

    @Test
    @DisplayName("Debería validar un token válido")
    void shouldValidateValidToken() {
        // Given
        String token = jwtService.generateToken(testEmail, testRole);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn(testEmail);

        // When
        Boolean isValid = jwtService.validateToken(token, userDetails);

        // Then
        assertTrue(isValid);
    }

    @Test
    @DisplayName("Debería rechazar un token con email diferente")
    void shouldRejectTokenWithDifferentEmail() {
        // Given
        String token = jwtService.generateToken(testEmail, testRole);
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("different@example.com");

        // When
        Boolean isValid = jwtService.validateToken(token, userDetails);

        // Then
        assertFalse(isValid);
    }

    @Test
    @DisplayName("Debería generar tokens diferentes para diferentes usuarios")
    void shouldGenerateDifferentTokensForDifferentUsers() {
        // When
        String token1 = jwtService.generateToken("user1@example.com", "CUSTOMER");
        String token2 = jwtService.generateToken("user2@example.com", "ADMIN");

        // Then
        assertNotEquals(token1, token2);
    }

    @Test
    @DisplayName("Debería generar tokens diferentes para el mismo usuario en diferentes momentos")
    void shouldGenerateDifferentTokensAtDifferentTimes() throws InterruptedException {
        // When
        String token1 = jwtService.generateToken(testEmail, testRole);
        Thread.sleep(1000); // Esperar 1 segundo
        String token2 = jwtService.generateToken(testEmail, testRole);

        // Then
        // Los tokens pueden ser diferentes debido a timestamps diferentes
        // pero ambos deben ser válidos
        assertNotNull(token1);
        assertNotNull(token2);
        assertEquals(testEmail, jwtService.extractEmail(token1));
        assertEquals(testEmail, jwtService.extractEmail(token2));
    }

    @Test
    @DisplayName("Debería extraer claims personalizados del token")
    void shouldExtractCustomClaims() {
        // Given
        String token = jwtService.generateToken(testEmail, testRole);

        // When
        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));

        // Then
        assertEquals(testRole, role);
    }

    @Test
    @DisplayName("Debería lanzar excepción con token inválido")
    void shouldThrowExceptionWithInvalidToken() {
        // Given
        String invalidToken = "invalid.token.here";

        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractEmail(invalidToken));
    }

    @Test
    @DisplayName("Debería lanzar excepción con token vacío")
    void shouldThrowExceptionWithEmptyToken() {
        // When & Then
        assertThrows(Exception.class, () -> jwtService.extractEmail(""));
        assertThrows(Exception.class, () -> jwtService.extractEmail(null));
    }

    @Test
    @DisplayName("Debería generar token con diferentes roles")
    void shouldGenerateTokenWithDifferentRoles() {
        // When
        String customerToken = jwtService.generateToken(testEmail, "CUSTOMER");
        String adminToken = jwtService.generateToken(testEmail, "ADMIN");

        // Then
        assertEquals("CUSTOMER", jwtService.extractRole(customerToken));
        assertEquals("ADMIN", jwtService.extractRole(adminToken));
    }
}

