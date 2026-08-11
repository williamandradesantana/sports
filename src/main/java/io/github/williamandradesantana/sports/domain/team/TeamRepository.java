package io.github.williamandradesantana.sports.domain.team;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface TeamRepository {
    Optional<Team> findById(UUID id);
    Optional<Team> findByExternalId(Long externalId);
    Page<Team> findAll(Pageable pageable);
    void save(Team team);
}
