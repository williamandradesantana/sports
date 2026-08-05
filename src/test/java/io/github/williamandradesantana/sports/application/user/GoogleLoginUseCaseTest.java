package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GoogleLoginUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private TokenService tokenService;

    private GoogleLoginUseCase googleLoginUseCase;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        googleLoginUseCase = new GoogleLoginUseCase(userRepository, permissionRepository, tokenService);
    }

    @Test
    @DisplayName("Test: logging in with Google using an existing email should link to the existing account")
    void test_GoogleLogin_WithExistingEmail_ShouldLinkToExistingAccount() {
        Permission commonUser = new Permission(UUID.randomUUID(), PermissionName.COMMON_USER);
        User existingUser = new User(
            UUID.randomUUID(), "wbs", "William Santana", "wbs@example.com", "encoded-hash",
            AuthProvider.LOCAL,
            true, true, true, true,
            Set.of(commonUser)
        );

        when(userRepository.findByEmail("wbs@example.com")).thenReturn(Optional.of(existingUser));
        when(tokenService.generateToken(eq("wbs"), anySet())).thenReturn("jwt-token");

        String token = googleLoginUseCase.execute(new GoogleProfileCommand("wbs@example.com", "William Santana"));

        assertEquals("jwt-token", token);
        verify(userRepository, never()).save(any());
        verify(permissionRepository, never()).findByDescription(any());
    }

    @Test
    @DisplayName("Test: logging in with Google using a new email should create a GOOGLE account")
    void test_GoogleLogin_WithNewEmail_ShouldCreateGoogleAccount() {
        Permission commonUser = new Permission(UUID.randomUUID(), PermissionName.COMMON_USER);

        when(userRepository.findByEmail("newperson@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("newperson")).thenReturn(Optional.empty());
        when(permissionRepository.findByDescription(PermissionName.COMMON_USER))
                .thenReturn(Optional.of(commonUser));
        when(tokenService.generateToken(eq("newperson"), anySet())).thenReturn("jwt-token");

        String token = googleLoginUseCase.execute(new GoogleProfileCommand("newperson@example.com", "New Person"));

        assertEquals("jwt-token", token);

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        User savedUser = captor.getValue();
        assertEquals(AuthProvider.GOOGLE, savedUser.getAuthProvider());
        assertNull(savedUser.getPassword());
        assertEquals("newperson", savedUser.getUsername());
    }

    @Test
    @DisplayName("Test: username generation should avoid collision with existing usernames")
    void test_UsernameGeneration_ShouldAvoidCollision() {
        when(userRepository.findByEmail("wbs@example.com")).thenReturn(Optional.empty());
        when(userRepository.findByUsername("wbs")).thenReturn(Optional.of(mock(User.class)));
        when(userRepository.findByUsername("wbs1")).thenReturn(Optional.empty());
        when(permissionRepository.findByDescription(PermissionName.COMMON_USER))
                .thenReturn(Optional.of(new Permission(UUID.randomUUID(), PermissionName.COMMON_USER)));
        when(tokenService.generateToken(eq("wbs1"), anySet())).thenReturn("jwt-token");

        googleLoginUseCase.execute(new GoogleProfileCommand("wbs@example.com", "William Santana"));

        var captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(captor.capture());
        assertEquals("wbs1", captor.getValue().getUsername());
    }
}