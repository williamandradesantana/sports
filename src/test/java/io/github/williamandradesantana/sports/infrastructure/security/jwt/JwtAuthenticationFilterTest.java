package io.github.williamandradesantana.sports.infrastructure.security.jwt;

import io.github.williamandradesantana.sports.domain.user.PermissionName;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtService jwtService;
    private JwtAuthenticationFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain filterChain;

    @BeforeEach
    void setup() {
        // Given - Arrange
        jwtService = new JwtService(new JwtProperties("secret-key-test", 3600));
        filter = new JwtAuthenticationFilter(jwtService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        filterChain = mock(FilterChain.class);
    }

    @AfterEach
    void afterEach() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Test: valid token should authenticate the request with roles from the token")
    void test_WhenTokenIsValid_ShouldAuthenticateTheRequestWithRolesFromTheToken() throws Exception {
        String expectedName = "wbs";
        String token = jwtService.generateToken("wbs", Set.of(PermissionName.COMMON_USER, PermissionName.ADMIN));

        // When - Act
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        filter.doFilter(request, response, filterChain);

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        // Then - Assert
        assertNotNull(authentication, () -> "Expected authentication to be set for a valid token");
        assertEquals(expectedName, authentication.getName(), () -> "The name not matches!");
        assertTrue(authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals(PermissionName.COMMON_USER)));
        assertTrue(authentication.getAuthorities()
                .stream().anyMatch(authority -> authority.getAuthority().equals(PermissionName.ADMIN)));

        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Test: missing Authorization header should not authenticate, but still continue the chain")
    void test_MissingHeader_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);
        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), () -> "The authentication must be null");
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Test: invalid token should not authenticate, but still continue the chain")
    void test_InvalidToken_ShouldNotAuthenticate() throws Exception {
        when(request.getHeader("Authorization")).thenReturn("Bearer invalid.token.header");
        filter.doFilter(request, response, filterChain);

        assertNull(SecurityContextHolder.getContext().getAuthentication(), () -> "The authentication must be null");
        verify(filterChain).doFilter(request, response);
    }
}