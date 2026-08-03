package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.*;
import io.github.williamandradesantana.sports.domain.user.exceptions.UsernameAlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterUserUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PermissionRepository permissionRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private RegisterUserUseCase useCase;
    private RegisterUserCommand userCommand;
    private Permission permission;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        useCase = new RegisterUserUseCase(userRepository, permissionRepository, passwordEncoder);
        permission = new Permission(UUID.randomUUID(), PermissionName.COMMON_USER);
        userCommand = new RegisterUserCommand("wbs", "william santana", "password-123");
    }

    @Test
    @DisplayName("Test: registering a new user with success")
    void test_RegisteringANewUser_ShouldSucceed() {
        String expectedUsername = "wbs";
        String expectedFullName = "william santana";
        String expectedPassword = "encoded-hash";

        when(userRepository.findByUsername("wbs")).thenReturn(Optional.empty());
        when(permissionRepository.findByDescription(permission.getDescription())).thenReturn(Optional.of(permission));
        when(passwordEncoder.encode("password-123")).thenReturn("encoded-hash");

        User created = useCase.execute(userCommand);

        assertNotNull(created, () -> "The user cannot be null!");
        assertEquals(expectedUsername, created.getUsername(), () -> "The username not matches!");
        assertEquals(expectedFullName, created.getFullName(), () -> "The fullName not matches!");
        assertEquals(expectedPassword, created.getPassword(), () -> "The password not matches!");
        assertTrue(created.getPermissions().contains(permission), () -> "The user should have the permission expected!");
        verify(userRepository, times(1)).save(created);
    }

    @Test
    @DisplayName("Test: registering an existing username should throw UsernameAlreadyExistsException")
    void test_RegisteringExistingUsername_ShouldThrow() {
        when(userRepository.findByUsername("wbs")).thenReturn(Optional.of(mock(User.class)));

        assertThrows(UsernameAlreadyExistsException.class, () -> useCase.execute(userCommand));
        verify(userRepository, never()).save(any());
    }
}