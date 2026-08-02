package io.github.williamandradesantana.sports.domain.user;

import java.util.Optional;

public interface PermissionRepository {
    Optional<Permission> findByDescription(String description);
}
