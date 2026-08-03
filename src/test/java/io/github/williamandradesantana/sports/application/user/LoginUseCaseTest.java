package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.Permission;
import io.github.williamandradesantana.sports.domain.user.PermissionName;
import io.github.williamandradesantana.sports.domain.user.User;
import io.github.williamandradesantana.sports.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginUseCaseTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private LoginUseCase loginUseCase;

    private Permission permission;
    private User user;

    @BeforeEach
    void setup() {
        // Given - Arrange
        permission = new Permission(UUID.randomUUID(), PermissionName.COMMON_USER);
        user = new User(
            UUID.randomUUID(), "william", "william santana", "password",
            true, true, true, true,
            Set.of(permission)
        );
        loginUseCase = new LoginUseCase(tokenService, authenticationManager, userRepository);
    }

    @Test
    @DisplayName("Test: when a user sends corrects credentials should authenticate user")
    void test_WhenAUserSendCorrectCredentials_ShouldAuthenticateUser() {
        String expectedToken = "jwt-token";

        // When - Act
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
        when(tokenService.generateToken(eq(user.getUsername()), anySet())).thenReturn(expectedToken);

        String token = loginUseCase.execute(new LoginCommand(user.getUsername(), user.getPassword()));
        ArgumentCaptor<Set<String>> rolesCaptor = ArgumentCaptor.forClass(Set.class);

        // Then - Assert
        assertEquals(expectedToken, token, () -> "Token not matches!");
        verify(tokenService).generateToken(eq(user.getUsername()), rolesCaptor.capture());
        assertEquals(Set.of(PermissionName.COMMON_USER), rolesCaptor.getValue(),
                () -> "Expected roles to come from the domain User, not from Authentication");
    }

    @Test
    @DisplayName("Test: when a user sends incorrect credentials should return BadCredentialsException")
    void test_WhenAUserSendInCorrectCredentials_ShouldReturnBadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials!"));

        assertThrows(BadCredentialsException.class, () ->
                loginUseCase.execute(new LoginCommand("test", "wrong-password")));

        verifyNoInteractions(tokenService);
    }

    @Test
    @DisplayName("Test: when authenticated user is not found in repository should throw IllegalStateException")
    void test_WhenAuthenticatedUserNotFoundInRepository_ShouldThrow() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(userRepository.findByUsername(user.getUsername())).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () ->
                loginUseCase.execute(new LoginCommand(user.getUsername(), user.getPassword())));

        verifyNoInteractions(tokenService);
    }
}