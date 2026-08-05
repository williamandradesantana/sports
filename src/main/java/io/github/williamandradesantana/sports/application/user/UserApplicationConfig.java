package io.github.williamandradesantana.sports.application.user;

import io.github.williamandradesantana.sports.domain.user.PermissionRepository;
import io.github.williamandradesantana.sports.domain.user.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserApplicationConfig {

    @Bean
    public RegisterUserUseCase registerUserUseCase(
        UserRepository userRepository,
        PermissionRepository permissionRepository,
        PasswordEncoder passwordEncoder
    ) {
        return new RegisterUserUseCase(userRepository, permissionRepository, passwordEncoder);
    }

    @Bean
    public LoginUseCase loginUseCase(
        TokenService tokenService,
        AuthenticationManager authenticationManager,
        UserRepository userRepository
    ) {
        return new LoginUseCase(tokenService, authenticationManager, userRepository);
    }

    @Bean
    public GoogleLoginUseCase googleLoginUseCase(
        UserRepository userRepository,
        PermissionRepository permissionRepository,
        TokenService tokenService
    ) {
        return new GoogleLoginUseCase(userRepository, permissionRepository, tokenService);
    }
}
