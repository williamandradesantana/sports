package io.github.williamandradesantana.sports.domain.user;

import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPasswordException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionException;
import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidUsernameException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    private User user;
    private Permission permission;

    @BeforeEach
    void setup() {
        // given - arrange
        permission = new Permission(UUID.randomUUID(), "COMMON_USER");
        user = new User(
            UUID.randomUUID(),
            "user",
            "fullName",
            "user@gmail.com",
            "password",
            AuthProvider.LOCAL,
            true,
            true,
            true,
            true,
            Set.of(permission)
        );
    }

    @AfterEach
    void afterEach() {
        user = null;
    }

    @Test
    @DisplayName("Test: creating a user with success")
    void test_CreatingAUserWithSuccess() {
        String expectedUsername = "user";
        String expectedFullName = "fullName";
        String expectedPassword = "password";
        String expectedPermission = "COMMON_USER";

        assertNotNull(user);
        assertEquals(expectedUsername, user.getUsername(), () -> "The username not matches!");
        assertEquals(expectedFullName, user.getFullName(), () -> "The fullName not matches!");
        assertEquals(expectedPassword, user.getPassword(), () -> "The password not matches!");
        assertEquals(expectedPermission, user.getPermissions().contains(permission) ? permission.getDescription() : null, () -> "Expected permission not found");
    }

    @Test
    @DisplayName("Test: when creating a user the username cannot be null should return throw InvalidUsernameException")
    void test_WhenCreatingAUserTheUsernameCannotBeNull_ShouldReturnThrow() {
        String expectedMessage = "Username cannot be null or blank";

        InvalidUsernameException exception = assertThrows(
            InvalidUsernameException.class, () -> user.setUsername(null),
            () -> "The username cannot be null"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }

    @Test
    @DisplayName("Test: when creating a user the username cannot be blank should return throw InvalidUsernameException")
    void test_WhenCreatingAUserTheUsernameCannotBeBlank_ShouldReturnThrow() {
        String expectedMessage = "Username cannot be null or blank";

        InvalidUsernameException exception = assertThrows(
            InvalidUsernameException.class, () -> user.setUsername(""),
            () -> "The username cannot be blank"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }

    @Test
    @DisplayName("Test: when creating a user the password cannot be null should return throw InvalidPasswordException")
    void test_WhenCreatingAUserThePasswordCannotBeNull_ShouldReturnThrow() {
        String expectedMessage = "Password must be at least 8 characters long";

        InvalidPasswordException exception = assertThrows(
            InvalidPasswordException.class, () -> user.setPassword(null),
            () -> "The password cannot be null"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }

    @Test
    @DisplayName("Test: when creating a user the password must be at least 8 characters long should return throw InvalidPasswordException")
    void test_WhenCreatingAUserThePasswordMustBeAtLeast8Characters_ShouldReturnThrow() {
        String expectedMessage = "Password must be at least 8 characters long";

        InvalidPasswordException exception = assertThrows(
            InvalidPasswordException.class, () -> user.setPassword("1234567"),
            () -> "The password must be at least 8 characters long"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }

    @Test
    @DisplayName("Test: granting a permission should add it to the user's permissions")
    void test_GrantingAPermission_ShouldAddToPermissions() {
        user.grantPermission(permission);

        assertTrue(user.getPermissions().contains(permission), () -> "Expected permission to be present after granting");
    }

    @Test
    @DisplayName("Test: granting a null permission should throw InvalidPermissionException")
    void test_GrantingANullPermission_ShouldThrow() {
        String expectedMessage = "Permission cannot be null";

        InvalidPermissionException exception = assertThrows(
            InvalidPermissionException.class, () -> user.grantPermission(null),
            () -> "Permission cannot be null"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }

    @Test
    @DisplayName("Test: revoking a permission should remove it from the user's permissions")
    void test_RevokingAPermission_ShouldRemoveFromPermissions() {
        user.grantPermission(permission);
        user.revokePermission(permission);

        assertFalse(user.getPermissions().contains(permission), () -> "Expected permission to be absent after revoking");
    }

    @Test
    @DisplayName("Test: get permissions should return an unmodifiable set")
    void test_GetPermissions_ShouldReturnUnmodifiableSet() {
        assertThrows(UnsupportedOperationException.class,
            () -> user.getPermissions().add(new Permission(UUID.randomUUID(), "ADMIN")),
            () -> "Expected getPermissions() to return an unmodifiable set"
        );
    }

    @Test
    @DisplayName("Test: creating a google user without password should succeed")
    void test_CreatingGoogleUser_WithoutPassword_ShouldSucceed() {
        AuthProvider expectedProvider = AuthProvider.GOOGLE;
        user = new User(
            UUID.randomUUID(), "user", "fullName", "user@gmail.com", null,
            AuthProvider.GOOGLE,
            true, true, true, true,
            Set.of(permission)
        );

        assertNull(user.getPassword(), () -> "The password must be null!");
        assertEquals(expectedProvider, user.getAuthProvider(), () -> "The provider not matches!");
    }

    @Test
    @DisplayName("Test: calling setPassword on a Google user should throw InvalidPasswordException")
    void test_SettingPasswordOnGoogleUser_ShouldThrow() {
        user = new User(
            UUID.randomUUID(), "user", "fullName", "user@gmail.com", null,
            AuthProvider.GOOGLE,
            true, true, true, true,
            Set.of(permission)
        );

        assertThrows(
            InvalidPasswordException.class,
            () -> user.setPassword("password"),
            () -> "The provider google cannot have password"
        );
    }
}