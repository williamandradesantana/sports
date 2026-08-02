package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import io.github.williamandradesantana.sports.domain.user.Permission;
import io.github.williamandradesantana.sports.domain.user.User;
import io.github.williamandradesantana.sports.domain.user.UserRepository;
import io.github.williamandradesantana.sports.infrastructure.persistence.PostgresIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(UserPersistenceConfig.class)
class UserRepositoryImplTest extends PostgresIntegrationTest {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PermissionJpaRepository permissionJpaRepository;

    private User user;
    private Permission permission;

    @BeforeEach
    void setup() {
        // given - arrange
        permission = fetchSeededPermission("ADMIN");
        user = new User(
            UUID.randomUUID(), "william", "william santana", "password",
            true, true, true, true,
            Set.of(permission)
        );
    }

    @AfterEach
    void afterEach() {
        permission = null;
        user = null;
    }

    @Test
    @DisplayName("Test: saving a user and finding by username should return the same data")
    void test_SavingAUser_ShouldBeFoundByUsername() {
        String expectedUsername = user.getUsername();

        // when - act
        repository.save(user);
        Optional<User> found = repository.findByUsername(expectedUsername);

        // assert - that
        assertTrue(found.isPresent(), () -> "Expected user to be found after saving");
        assertEquals(expectedUsername, found.get().getUsername(), () -> "Found user incorrect username");
        assertTrue(
            found.get().getPermissions().contains(permission),
            () -> "Expected saved permission to be present after reload!"
        );
    }

    private Permission fetchSeededPermission(String description) {
        return permissionJpaRepository.findByDescription(description)
                .map(entity -> new Permission(entity.getId(), entity.getDescription()))
                .orElseThrow(() -> new IllegalStateException("Seed permission not found!"));
    }
}