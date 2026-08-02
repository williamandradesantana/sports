package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface PermissionJpaRepository extends JpaRepository<PermissionJpaEntity, UUID> {
    Optional<PermissionJpaEntity> findByDescription(String description);
}
