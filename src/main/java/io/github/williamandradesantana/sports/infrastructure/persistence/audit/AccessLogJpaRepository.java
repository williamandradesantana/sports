package io.github.williamandradesantana.sports.infrastructure.persistence.audit;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccessLogJpaRepository extends JpaRepository<AccessLogJpaEntity, UUID> {
    Page<AccessLogJpaEntity> findByUsername(String username, Pageable pageable);
}
