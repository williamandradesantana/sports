package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import io.github.williamandradesantana.sports.domain.user.Permission;
import io.github.williamandradesantana.sports.domain.user.PermissionRepository;

import java.util.Optional;

public class PermissionRepositoryImpl implements PermissionRepository {

    private final PermissionJpaRepository jpaRepository;
    private final PermissionMapper mapper;

    public PermissionRepositoryImpl(PermissionJpaRepository jpaRepository, PermissionMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Permission> findByDescription(String description) {
        return jpaRepository.findByDescription(description).map(mapper::toDomain);
    }
}
