package io.github.williamandradesantana.sports.infrastructure.persistence.user;

import io.github.williamandradesantana.sports.domain.user.PermissionRepository;
import io.github.williamandradesantana.sports.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserPersistenceConfig {

    @Bean
    public UserMapper userMapper(PermissionMapper permissionMapper) {
        return new UserMapper(permissionMapper);
    }

    @Bean
    public PermissionMapper permissionMapper() {
        return new PermissionMapper();
    }

    @Bean
    public UserRepository userRepository(UserJpaRepository jpaRepository, UserMapper mapper) {
        return new UserRepositoryImpl(jpaRepository, mapper);
    }

    @Bean
    public PermissionRepository permissionRepository(PermissionJpaRepository jpaRepository, PermissionMapper mapper) {
        return new PermissionRepositoryImpl(jpaRepository, mapper);
    }
}
