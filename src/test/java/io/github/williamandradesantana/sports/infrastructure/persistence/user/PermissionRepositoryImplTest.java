package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import io.github.williamandradesantana.sports.domain.user.Permission;
import io.github.williamandradesantana.sports.domain.user.PermissionRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserPersistenceConfig.class)
class PermissionRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private PermissionRepository repository;

    private Permission permission;

    @BeforeEach
    void setup() {
        permission = new Permission(UUID.randomUUID(), "ADMIN");
    }

    @AfterEach
    void afterEach() {
        permission = null;
    }

    @Test
    @DisplayName("Test: finding a seeded permission by description should return it")
    void test_FindingSeededPermission_ShouldReturnIt() {
        String expectedDescription = "ADMIN";
        Optional<Permission> found = repository.findByDescription(permission.getDescription());

        assertTrue(found.isPresent(), () -> "Expected description to be found after saving");
        assertEquals(expectedDescription, found.get().getDescription());
    }

    @Test
    @DisplayName("Test: finding a non-existent permission by description should return empty")
    void test_FindingNonExistentPermission_ShouldReturnEmpty() {
        Optional<Permission> found = repository.findByDescription("DOES_NOT_EXIST");

        assertTrue(found.isEmpty(), () -> "Expected empty when permission description does not exist");
    }
}