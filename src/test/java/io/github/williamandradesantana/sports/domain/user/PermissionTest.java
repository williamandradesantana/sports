package io.github.williamandradesantana.sports.domain.user;

import io.github.williamandradesantana.sports.domain.user.exceptions.InvalidPermissionDescriptionException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PermissionTest {

    private Permission permission;

    @BeforeEach
    void setup() {
        permission = new Permission(UUID.randomUUID(), "PERMISSION_TEST");
    }

    @AfterEach
    void afterEach() {
        permission = null;
    }

    @Test
    @DisplayName("Test: creating a permission with success")
    void test_CreatingAPermissionWithSuccess() {
        String expectedDescription = "PERMISSION_TEST";

        assertNotNull(permission, () -> "The permission cannot be null!");
        assertEquals(expectedDescription, permission.getDescription(), "The description not matches!");
    }

    @Test
    @DisplayName("Test: when creating a permission the description cannot be null should return throw InvalidPermissionDescriptionException")
    void test_WhenCreatingAPermissionTheDescriptionCannotBeNull_ShouldReturnThrow() {
        String expectedMessage = "Permission description is invalid";

        InvalidPermissionDescriptionException exception = assertThrows(
            InvalidPermissionDescriptionException.class, () -> permission.setDescription(null),
            () -> "The description cannot be null"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }

    @Test
    @DisplayName("Test: when creating a permission the description cannot be blank should return throw InvalidPermissionDescriptionException")
    void test_WhenCreatingAPermissionTheDescriptionCannotBeBlank_ShouldReturnThrow() {
        String expectedMessage = "Permission description is invalid";

        InvalidPermissionDescriptionException exception = assertThrows(
            InvalidPermissionDescriptionException.class, () -> permission.setDescription(""),
            () -> "The description cannot be blank"
        );

        assertEquals(expectedMessage, exception.getMessage(), () -> "The error message not matches!");
    }
}