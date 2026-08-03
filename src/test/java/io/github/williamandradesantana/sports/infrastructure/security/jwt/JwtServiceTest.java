package io.github.williamandradesantana.sports.infrastructure.security.jwt;

import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.williamandradesantana.sports.domain.user.PermissionName;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setup() {
        jwtService = new JwtService(new JwtProperties("test-secret-key", 3600));
    }

    @AfterEach
    void afterEach() {
        jwtService = null;
    }

    @Test
    @DisplayName("Test: generating and verifying a token should return the original subject and roles")
    void test_GeneratingAndVerifyingToken_ShouldRoundTrip() {
        String token = jwtService.generateToken("wbs", Set.of(PermissionName.COMMON_USER, PermissionName.ADMIN));
        TokenClaims tokenClaims = jwtService.verifyAndExtractClaims(token);

        assertEquals("wbs", tokenClaims.username());
        assertEquals(Set.of(PermissionName.COMMON_USER, PermissionName.ADMIN), tokenClaims.roles());
    }

    @Test
    @DisplayName("Test: verifying a token signed with a different secret should throw JWTVerificationException")
    void test_VerifyingTokenSignedWithDifferentSecret_ShouldThrow() {
        JwtService anotherJwtService = new JwtService(new JwtProperties("different-secret-key", 3600));
        String token = anotherJwtService.generateToken("wbs", Set.of(PermissionName.COMMON_USER, PermissionName.ADMIN));

        assertThrows(JWTVerificationException.class, () -> jwtService.verifyAndExtractClaims(token));
    }

    @Test
    @DisplayName("Test: verifying a tampered token should throw JWTVerificationException")
    void test_VerifyingTamperedToken_ShouldThrow() {
        String token = jwtService.generateToken("wbs", Set.of(PermissionName.COMMON_USER, PermissionName.ADMIN));
        String tampered = token.substring(0, token.length() - 1) + (token.endsWith("A") ? "B" : "A");

        assertThrows(JWTVerificationException.class, () -> jwtService.verifyAndExtractClaims(tampered));
    }
}