package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import io.github.williamandradesantana.sports.domain.user.Permission;
import io.github.williamandradesantana.sports.domain.user.User;

import java.util.Set;
import java.util.stream.Collectors;

public class UserMapper {

    private final PermissionMapper permissionMapper;

    public UserMapper(PermissionMapper permissionMapper) {
        this.permissionMapper = permissionMapper;
    }

    public User toDomain(UserJpaEntity entity) {
        Set<Permission> permissions = entity.getPermissions()
                .stream().map(permissionMapper::toDomain)
                .collect(Collectors.toSet());
        return new User(
            entity.getId(),
            entity.getUserName(),
            entity.getFullName(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getAuthProvider(),
            entity.isAccountNonExpired(),
            entity.isAccountNonLocked(),
            entity.isCredentialsNonExpired(),
            entity.isEnabled(),
            permissions
        );
    }

    public UserJpaEntity toJpaEntity(User user) {
        Set<PermissionJpaEntity> permissions = user.getPermissions()
                .stream().map(permissionMapper::toJpaEntity)
                .collect(Collectors.toSet());

        return new UserJpaEntity(
            user.getId(),
            user.getUsername(),
            user.getFullName(),
            user.getEmail(),
            user.getPassword(),
            user.getAuthProvider(),
            user.isAccountNonExpired(),
            user.isAccountNonLocked(),
            user.isCredentialsNonExpired(),
            user.isEnabled(),
            permissions
        );
    }
}
