package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import io.github.williamandradesantana.sports.domain.user.Permission;

public class PermissionMapper {

    public Permission toDomain(PermissionJpaEntity entity) {
        return new Permission(entity.getId(), entity.getDescription());
    }

    public PermissionJpaEntity toJpaEntity(Permission permission) {
        return new PermissionJpaEntity(permission.getId(), permission.getDescription());
    }
}
